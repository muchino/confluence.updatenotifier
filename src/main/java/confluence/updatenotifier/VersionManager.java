/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package confluence.updatenotifier;

import com.atlassian.confluence.json.parser.JSONObject;
import com.atlassian.confluence.util.GeneralUtil;
import com.atlassian.core.util.DateUtils;
import com.atlassian.plugin.PluginAccessor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;

/**
 *
 * @author oli
 */
public class VersionManager {

    private static final ConfluenceVersion currentVersion = new ConfluenceVersion(GeneralUtil.getVersionNumber());
    private static final String URL = "https://my.atlassian.com/download/feeds/current/confluence.json";
    private static final String UPGRADE_NOTES = "upgradeNotes";
    private static final String VERSION = "version";
    private DateTime lastCheck = null;
    private ConfluenceVersion onlineVersion = null;
    private Minutes minInterval = Minutes.minutes(1);

    public ConfluenceVersion getNewestConfluenceVersion() {
        if (this.isCheckNecessary()) {
            this.lastCheck = new DateTime();
            try {
                StringBuilder sb;
                String line;
                HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();
                conn.setConnectTimeout(2000); //set timeout to 2 seconds
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    conn.connect();
                    InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
                    BufferedReader buff = new BufferedReader(in);
                    sb = new StringBuilder();
                    while ((line = buff.readLine()) != null) {
                        sb.append(line);
                    }
                    if (sb.length() > 11) {
                        JSONObject jsonObject = new JSONObject(sb.subSequence(11, sb.length() - 2).toString());
                        String version = (String) jsonObject.get(VERSION);
                        this.onlineVersion = new ConfluenceVersion(version);
                        this.onlineVersion.setReleaseNotesURL((String) jsonObject.get(UPGRADE_NOTES));
                    }
                }
            } catch (Exception e) {
            }
            if (this.onlineVersion == null) {
                this.onlineVersion = new ConfluenceVersion(GeneralUtil.getVersionNumber());
            }
        }
        return this.onlineVersion;
    }

    private boolean isCheckNecessary() {
        if (lastCheck == null || onlineVersion == null) {
            return true;
        }

        Minutes Interval = Minutes.minutesBetween(lastCheck, new DateTime());
        return Interval.isGreaterThan(minInterval);
    }

    public boolean isNewVersionAvailable() {
        return currentVersion.isOlderThan(getNewestConfluenceVersion());
    }
}
