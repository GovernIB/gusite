package es.caib.gusite.micropersistence.util;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class UsernamePasswordCallbackHandler implements CallbackHandler {
	private String username;
	private String password;

	public UsernamePasswordCallbackHandler(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		for (Callback callback : callbacks) {
			if (callback instanceof NameCallback) {
				NameCallback ncb = (NameCallback) callback;
				ncb.setName(this.username);
			} else if (callback instanceof PasswordCallback) {
				PasswordCallback pcb = (PasswordCallback) callback;
				pcb.setPassword(this.password.toCharArray());
			} else {
				throw new UnsupportedCallbackException(callback,
						"Unrecognized Callback");
			}
		}
	}
}
