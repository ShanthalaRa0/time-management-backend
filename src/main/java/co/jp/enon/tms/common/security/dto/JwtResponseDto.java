package co.jp.enon.tms.common.security.dto;

import java.util.List;

public class JwtResponseDto {
	private String token;
	private String type = "Bearer";
	private Integer id;
	private String username;
	private List<String> authorities;

	public JwtResponseDto(String accessToken, Integer id, String username, List<String> authorities) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		//this.status = status;
		this.authorities = authorities;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/*public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	} */

	public List<String> getAuthorities() {
		return authorities;
	}
}
