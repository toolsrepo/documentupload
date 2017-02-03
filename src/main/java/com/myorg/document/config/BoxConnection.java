package com.myorg.document.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;

@Configuration
public class BoxConnection {
	
	@Value("${box.client_id}")
	private String clientId;
	
	@Value("${box.client_secret}")
	private String clientSecret;
	
	@Value("${box.developertoken}")
	private String developerToken;
	
	@Value("${box.mortgageBasePath}")
	private String boxMortgageBasePath;
	
	public static final int BASE_MAX_DEPTH = 2;
	
	public BoxAPIConnection getAPIConnectionByDeveloperToken() {
		BoxAPIConnection api = new BoxAPIConnection(developerToken);
		return api;
	}
	
	public static BoxFolder getFolder(BoxFolder folder, int depth, String searchFolder) {
    	BoxFolder targetfolder = null;
        for (BoxItem.Info itemInfo : folder) {
            if (searchFolder.equalsIgnoreCase(itemInfo.getName())) {
            	targetfolder = (BoxFolder) itemInfo.getResource();
            	break;
            }
            if (itemInfo instanceof BoxFolder.Info) {
                BoxFolder childFolder = (BoxFolder) itemInfo.getResource();
                if (depth < BASE_MAX_DEPTH) {
                    getFolder(childFolder, depth + 1, searchFolder);
                }
            }
        }
        return targetfolder;
    }

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getDeveloperToken() {
		return developerToken;
	}

	public void setDeveloperToken(String developerToken) {
		this.developerToken = developerToken;
	}

	public String getBoxMortgageBasePath() {
		return boxMortgageBasePath;
	}

	public void setBoxMortgageBasePath(String boxMortgageBasePath) {
		this.boxMortgageBasePath = boxMortgageBasePath;
	}
	
}
