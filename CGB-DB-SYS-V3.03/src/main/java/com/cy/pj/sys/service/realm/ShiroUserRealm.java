package com.cy.pj.sys.service.realm;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;

/**
 * 借助此对象基于数据层对象“获取”认证信息，权限信息
 * @author Administrator
 */
@Service
public class ShiroUserRealm extends AuthorizingRealm{//我们写的realm必须符合Realm规范

	@Autowired
	private SysUserDao sysUserDao;
	/**
	 * 指定加密算法和次数(这里可以重写setCredentialsMatcher或getCredentialsMatcher)
	 */
//	@Override
//	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
//	  //1，构建加密对象并指定算法
//		HashedCredentialsMatcher cMatcher=new HashedCredentialsMatcher("MD5");
//	  //2.设置加密次数
//		cMatcher.setHashIterations(1);
//	    super.setCredentialsMatcher(cMatcher);//注意此位置的值
//	}
//	
	@Override
	public CredentialsMatcher getCredentialsMatcher() {
		//1，构建加密对象并指定算法
		HashedCredentialsMatcher cMatcher=new HashedCredentialsMatcher("MD5");
		//2.设置加密次数
		cMatcher.setHashIterations(1);
		return cMatcher;
	}
    /**
     * 基于此方法获取用户信息并封装,然后将封装的结果传递给SecurityManager对象，由此Manager
     * 对象调用authenticate方法完成认证操作。
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		//1.基于参数token获取客户端输入的用户名(当前应用，这里的token具体类型为UsernamePasswordToken)
		UsernamePasswordToken uToken=(UsernamePasswordToken)token;
		String username=uToken.getUsername();
		//2.基于用户名查找用户信息并进行校验(用户是否存在，是否被禁用了)
		SysUser user=sysUserDao.findUserByUserName(username);
		if(user==null)
			throw new UnknownAccountException();
		if(user.getValid()==0) 
			throw new LockedAccountException();
		//3.对查询到的用户信息进行封装并返回。
		ByteSource credentialsSalt=ByteSource.Util.bytes(user.getSalt());
		//ByteSource credentialsSalt=new SimpleByteSource(user.getSalt());
		SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(
				user,//principal 用户身份，结合业务自己赋予具体对象
				user.getPassword(),//hashedCredentials 已加密的密码
				credentialsSalt,//credentialsSalt 对登录密码进行加密时使用的盐
				getName());//realmName
		return info;
	}
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysMenuDao sysMenuDao;
	/**
	 * 基于此方法进行授权信息的获取和封装。会将封装好的数据传递给SecurityManager对象，由此对象
	 * 进行用户权限检测和授权。
	 * 回顾：项目中业务的实现
	 * 1)角色和菜单是多对多的关系(关系的维护方在sys_role_menus)
	 * 2)用户和角色是多对多的关系(关系的维护方在sys_user_roles)
	 * 思考：业务中的资源分配过程
	 * 1)将菜单资源授权角色
	 * 2)角色授予用户对象
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		System.out.println("===doGetAuthorizationInfo()===");
	    //1.获取登陆用户信息
		SysUser user=(SysUser)principals.getPrimaryPrincipal();
		//2.基于登陆用户获取用户对应的角色id并校验
		List<Integer> roleIds=
		sysUserRoleDao.findRoleIdsByUserId(user.getId());
		if(roleIds==null||roleIds.size()==0)
		  throw new AuthorizationException();
		//3.基于角色id获取角色对应的菜单id并校验
		List<Integer> menuIds=
		sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[] {}));
		if(menuIds==null||menuIds.size()==0)
			throw new AuthorizationException();
		//4.基于菜单id获取菜单模块的授权标识(sys:user:update,sys:user:delete,...)
		List<String> permissionsList=
		sysMenuDao.findPermissions(menuIds.toArray(new Integer[] {}));
		if(permissionsList==null||permissionsList.size()==0)//添加操作时设置有误(没有给值)
			throw new AuthorizationException();//
		//5.封装结果并返回。
		Set<String> permissionsSet=new HashSet<>();
		for(String permission:permissionsList) {
			if(!StringUtils.isEmpty(permission)) {
			permissionsSet.add(permission);
			}
		}
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		info.setStringPermissions(permissionsSet);//HashSet(不允许重复)
		return info;//此对象会返回给securityManager对象
	}
}










