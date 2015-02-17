package es.caib.gusite.utilities.auth;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bry-at4 on 06/02/2015.
 */
public class CertsPrincipal implements Principal {

    private static final long serialVersionUID = 1L;
    ClientPrincipal wrappedPrincipal;

    public static CertsPrincipal getCurrent() throws NamingException {

        Subject s = (Subject) new InitialContext().lookup("java:comp/env/security/subject");
        if (s == null) {
            throw new NamingException("No current subject");
        }
        Set principals = s.getPrincipals(ClientPrincipal.class);
        if (principals == null || principals.isEmpty()) {
            return null;
        }
        Iterator it = principals.iterator();
        ClientPrincipal principal = (ClientPrincipal) it.next();
        return new CertsPrincipal(principal);
    }

    private CertsPrincipal (ClientPrincipal principal) {
        wrappedPrincipal = principal;
    }

    public boolean equals(Object obj) {
        if (obj instanceof CertsPrincipal) {
            return wrappedPrincipal.equals(((CertsPrincipal) obj).wrappedPrincipal);
        } else {
            return wrappedPrincipal.equals(obj);
        }
    }

    public String getFullName() {
        return wrappedPrincipal.getFullName();
    }

    public String getIntranetUser() {
        return wrappedPrincipal.getIntranetUser();
    }

    public String getName() {
        return wrappedPrincipal.getName();
    }

    public String getNif() {
        return wrappedPrincipal.getNif();
    }

    public String getNifResponsable() {
        return wrappedPrincipal.getNifResponsable();
    }

    public String getPolicy() {
        return wrappedPrincipal.getPolicy();
    }

    public String getProductor() {
        return wrappedPrincipal.getProductor();
    }

    public boolean isPersonaFisica() {
        return wrappedPrincipal.isPersonaFisica();
    }

    public boolean isPersonaJuridica() {
        return wrappedPrincipal.isPersonaJuridica();
    }

    public boolean isProxy() {
        return wrappedPrincipal.isProxy();
    }

    public String toString() {
        return wrappedPrincipal.toString();
    }

}
