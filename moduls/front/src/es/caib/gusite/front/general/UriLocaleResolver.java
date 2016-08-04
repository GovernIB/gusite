package es.caib.gusite.front.general;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

public class UriLocaleResolver extends AbstractLocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest req) {

		String path = req.getRequestURI();
		if (path.length() > 0) {
			String[] parts = path.split("/");
			if (parts.length > 3) {
				String lang = parts[3];
				if (lang.length() == 2 && lang.matches("[a-z][a-z]")) {
					return new Locale(lang.toUpperCase(), lang.toUpperCase());
				}

			}
		}
		if (req.getParameter("lang") != null) {
			return new Locale(req.getParameter("lang").toUpperCase(), req.getParameter("lang").toUpperCase());
		}

		return this.getDefaultLocale();

	}

	@Override
	public void setLocale(HttpServletRequest req, HttpServletResponse resp, Locale loc) {

		resp.setLocale(loc);

	}

}
