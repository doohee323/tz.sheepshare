package tz.basis.iam.core.jdbc.authentication;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * <pre>
 *
 * @see tz.basis.iam.core.jdbc.authentication.User
 *
 * @author TZ
 *
 */
public abstract class UserMapping implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 *
	 */
	protected Set<GrantedAuthority> authorities;

	protected String authorityString;

	protected boolean accountNonExpired;

	protected boolean accountNonLocked;

	protected boolean credentialsNonExpired;

	public UserMapping() {
		this.accountNonExpired = true;
		this.credentialsNonExpired = true;
		this.accountNonLocked = true;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(AuthorityUtils.NO_AUTHORITIES));
	}

	public UserMapping(boolean accountNonExpired,	boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}

	/**
	 * 인증된 사용자의 ID를 반환한다.
	 *
	 */
	public abstract String getUserId();

	/**
	 * 사용자의 ID를 저장한다.
	 *
	 * @param username
	 */
	public abstract void setUserId(String userId);

	/**
	 * 사용자의 Password를 반환한다.
	 */
	public abstract String getPassword();

	/**
	 * 사용자의 ID가 사용가능한지 여부를 반환한다.
	 */
	public abstract boolean isEnabled();

	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = Collections
				.unmodifiableSet(sortAuthorities(authorities));
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	/**
	 *
	 * 인증된 사용자의 권한을 문자열로 반환한다.
	 *
	 * @return
	 */
	public String getAuthorityString()	{
		if(!StringUtils.hasText(authorityString))	{
			Iterator<GrantedAuthority> it = authorities.iterator();

			while(it.hasNext()){
				GrantedAuthority grantedAuthority = it.next();

				if(StringUtils.hasText(authorityString)){
					authorityString += ",";
				}

				authorityString = authorityString + grantedAuthority.getAuthority();
			}
		}

		return authorityString;
	}

	private static SortedSet<GrantedAuthority> sortAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities,
				"Cannot pass a null GrantedAuthority collection");

		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<GrantedAuthority>(
				new AuthorityComparator());

		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority,
					"GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}

		return sortedAuthorities;
	}

	private static class AuthorityComparator implements
			Comparator<GrantedAuthority>, Serializable {
		public int compare(GrantedAuthority g1, GrantedAuthority g2) {

			if (g2.getAuthority() == null) {
				return -1;
			}

			if (g1.getAuthority() == null) {
				return 1;
			}

			return g1.getAuthority().compareTo(g2.getAuthority());
		}
	}
}
