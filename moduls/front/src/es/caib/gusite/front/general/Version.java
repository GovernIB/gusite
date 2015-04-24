package es.caib.gusite.front.general;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Datos de la versión de GUSITE
 * @author at4.net
 *
 */
@Component
public class Version {

	@Value( "${project.author}" )
	private String projectAuthor;
	
	@Value( "${microsites.version}" )
	private String micrositesVersion;

	@Value( "${microsites.build}" )
	private String micrositesBuild;
	
	@Value( "${project.version}" )
	private String projectVersion;
	
	@Value( "${java.version}" )
	private String javaVersion;

	@Value( "${svn.revision}" )
	private String svnRevision;
	
	@Value( "${microsites.name}" )
	private String micrositesName;

	public String getProjectAuthor() {
		return projectAuthor;
	}

	public void setProjectAuthor(String projectAuthor) {
		this.projectAuthor = projectAuthor;
	}

	public String getMicrositesVersion() {
		return micrositesVersion;
	}

	public void setMicrositesVersion(String micrositesVersion) {
		this.micrositesVersion = micrositesVersion;
	}

	public String getMicrositesBuild() {
		return micrositesBuild;
	}

	public void setMicrositesBuild(String micrositesBuild) {
		this.micrositesBuild = micrositesBuild;
	}

	public String getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getMicrositesName() {
		return micrositesName;
	}

	public void setMicrositesName(String micrositesName) {
		this.micrositesName = micrositesName;
	}

	public void setSvnRevision(String svnRevision) {
		this.svnRevision = svnRevision;
	}

	public String getSvnRevision() {
		return svnRevision;
	}
		
	
	
}
