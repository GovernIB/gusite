package es.caib.gusite.utilities.auth;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x509.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * Created by bru-at4 on 06/02/2015.
 */
public class ClientPrincipal implements Principal, Serializable {

    private static final long serialVersionUID = 1L;

    public static int ANONYMOUS_CREDENTIAL = 1;
    public static int PASSWORD_CREDENTIAL = 2;
    public static int SIGNATURE_CREDENTIAL = 3;

    private int credentialType;

    String nif;
    String fullName;
    String productor = null; // No se utiliza
    String intranetUser;
    String policy = null; // No se utiliza
    String givenName = null; // No se utiliza
    String surName = null; // No se utiliza
    boolean proxy = false; // No se utiliza
    String nifResponsable;
    boolean personaFisica;
    boolean personaJuridica;

    private static DERObjectIdentifier NIFOID = new DERObjectIdentifier("1.3.6.1.4.1.18838.1.1");
    private static DERObjectIdentifier productorOID = new DERObjectIdentifier("1.3.6.1.4.1.22896.1.1");
    private static DERObjectIdentifier proxyOID = new DERObjectIdentifier("1.3.6.1.4.1.22896.1.2");

    private X509Certificate cert;

    public static ClientPrincipal getCurrent() throws NamingException {

        Subject s = (Subject) new InitialContext().lookup("java:comp/env/security/subject");
        if (s == null) {
            throw new NamingException("No current subject");
        }
        Set principals = s.getPrincipals(ClientPrincipal.class);
        if (principals == null || principals.isEmpty()) {
            return null;
        }
        Iterator it = principals.iterator();
        return (ClientPrincipal) it.next();
    }

    private void init(X509Certificate cert) throws IOException, CertificateEncodingException {

        this.cert = cert;
        byte[] b = cert.getEncoded();
        ASN1InputStream asn1is = new ASN1InputStream(b);
        DERObject obj = asn1is.readObject();
        X509CertificateStructure certificate = new X509CertificateStructure((ASN1Sequence)obj);
        X509Name name = certificate.getSubject();
        this.personaFisica = false;
        this.personaJuridica = false;
        Vector v = name.getOIDs();
        Vector value = name.getValues();

        for (int tbs = 0; tbs < v.size(); ++tbs) {
            if(v.get(tbs).equals(X509Name.CN)) {
                this.processName(value.get(tbs).toString());
            }

            if (v.get(tbs).equals(X509Name.SURNAME)) {
                this.surName = value.get(tbs).toString();
            }

            if (v.get(tbs).equals(X509Name.GIVENNAME)) {
                this.givenName = value.get(tbs).toString();
            }

            if (v.get(tbs).equals(X509Name.CN)) {
                this.processName(value.get(tbs).toString());
            }

            if (v.get(tbs).equals(X509Name.SN)) {
                this.nif = value.get(tbs).toString();
                if (!this.personaJuridica) {
                    this.personaFisica = true;
                }
            }

            if (v.get(tbs).equals(NIFOID)) {
                this.nifResponsable = v.get(tbs).toString();
                this.personaFisica = false;
                this.personaJuridica = true;
            }
        }

        TBSCertificateStructure var17 = certificate.getTBSCertificate();
        X509Extensions exts = var17.getExtensions();
        X509Extension altName = exts.getExtension(X509Extensions.SubjectAlternativeName);
        if(altName != null) {
            asn1is = new ASN1InputStream(altName.getValue().getOctets());
            ASN1Sequence polext = (ASN1Sequence)asn1is.readObject();
            GeneralNames asn1seq = new GeneralNames(polext);
            GeneralName[] pol = asn1seq.getNames();

            for(int i = 0; i < pol.length; ++i) {
                if(pol[i].getTagNo() == 0) {
                    ASN1Sequence seq = (ASN1Sequence)pol[i].getName();
                    if(seq.getObjectAt(0).equals(productorOID)) {
                        this.productor = DERUTF8String.getInstance(seq.getObjectAt(1)).getString();
                    }

                    if(seq.getObjectAt(0).equals(proxyOID)) {
                        this.proxy = DERUTF8String.getInstance(seq.getObjectAt(1)).getString().equals("YES");
                    }
                }
            }
        }

        X509Extension var18 = exts.getExtension(X509Extensions.CertificatePolicies);
        if(var18 != null) {
            asn1is = new ASN1InputStream(var18.getValue().getOctets());
            ASN1Sequence var19 = (ASN1Sequence)asn1is.readObject();
            CertificatePolicies var20 = new CertificatePolicies(var19);
            this.policy = var20.getPolicy(0);
        }
    }

    public ClientPrincipal (X509Certificate cert) throws CertificateEncodingException, IOException {
        init(cert);
        credentialType = SIGNATURE_CREDENTIAL;
    }

    public ClientPrincipal(String user, String name, String nif) {
        intranetUser = user;
        fullName = name;
        this.nif = nif;
        credentialType = PASSWORD_CREDENTIAL;
    }

    public ClientPrincipal() {
        intranetUser = "nobody";
        fullName = "Usuari anònim";
        nif = null;
        credentialType = ANONYMOUS_CREDENTIAL;
    }

    public int getCredentialType() {
        return credentialType;
    }

    /* (non-Javadoc)
     * @see java.security.Principal#getName()
     */
    public String getName() {

        if (getIntranetUser() != null) {
            return getIntranetUser();
        }
        if (nif != null) {
            return nif;
        }
        return getFullName();
    }

    public String getNif() {
        return nif;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProductor() {
        return null;
    }

    public String getIntranetUser() {

        if (intranetUser != null) {
            return intranetUser;
        }

        if (productor == null) {
            return null;
        }
        String user = productor;
        while (user.length() < 5) {
            user = "0" + user;
        }
        user = "u" + user;
        return user;
    }

    public boolean isProxy() {
        return false;
    }

    public String getPolicy() {
        return null;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {

        // TODO Auto-generated method stub
        if (obj instanceof ClientPrincipal) {
            ClientPrincipal other = (ClientPrincipal) obj;
            if (cert != null && other.cert != null) {
                return cert.equals(other.cert);
            }
            if (intranetUser != null && other.intranetUser != null) {
                return intranetUser.equals(other.intranetUser);
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {

        String s;
        if (cert != null) {
            s = cert.getSubjectDN() + "(" + cert.getIssuerDN() + ")";
        } else {
            s = intranetUser;
        }
        s = s + " FullName:" + fullName + " NIF:" + nif;
        if (nifResponsable != null) {
            s = s + " NIF Responsable:" + nifResponsable;
        }
        return s;
    }

    /**
     * @return Returns the nifResponsable.
     */
    public String getNifResponsable() {
        return nifResponsable;
    }

    /**
     * @return Returns the personaFisica.
     */
    public boolean isPersonaFisica() {
        return personaFisica;
    }

    /**
     * @return Returns the personaJuridica.
     */
    public boolean isPersonaJuridica() {
        return personaJuridica;
    }

    public void setIntranetUser(String intranetUser) {
        if (getIntranetUser() == null && credentialType == SIGNATURE_CREDENTIAL) {
            this.intranetUser = intranetUser;
        }
    }

    private void processName(String cn) {

        int i;
        if (cn != null && cn.startsWith("NOMBRE ")) {
            i = cn.indexOf(" - ");
            if (i > 0 && cn.substring(i).startsWith(" - NIF ")) {
                this.nif = cn.substring(i + 7);
                this.fullName = cn.substring(7, i);
                this.personaFisica = true;
                this.personaJuridica = false;
            }

        } else if (cn != null && cn.startsWith("ENTIDAD ")) {
            this.personaFisica = false;
            this.personaJuridica = true;
            i = cn.indexOf(" - ");
            if (i > 0 && cn.substring(i).startsWith(" - CIF ")) {
                int j = cn.indexOf(" - ", i + 7);
                int k = cn.indexOf(" - NIF ", i + 7);
                if (j > 0 && k > 0) {
                    this.nif = cn.substring(i + 8, j);
                    this.fullName = cn.substring(7, i);
                    this.nifResponsable = cn.substring(k + 7);
                }
            }
        } else {
            this.fullName = cn;
        }
    }

}