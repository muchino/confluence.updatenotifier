package confluence.updatenotifier;

import com.atlassian.confluence.util.GeneralUtil;
import com.atlassian.plugin.web.model.WebPanel;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class NotifiyWebPanel implements WebPanel {

    private final VersionManager versionManager;

    public NotifiyWebPanel(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    @Override
    public String getHtml(Map<String, Object> map) {
        if (versionManager.isNewVersionAvailable()) {
            ConfluenceVersion version = versionManager.getNewestConfluenceVersion();
            StringBuilder sb = new StringBuilder();
            sb.append("<div class='aui-message info updatenoitifier-confluence'>");
            sb.append("<p>");
            sb.append(GeneralUtil.getI18n().getText("confluence.updatenotifier.newversion", Arrays.asList(version.getVersion())));
            if (StringUtils.isNotBlank(version.getReleaseNotesURL())) {
                sb.append(" ");
                sb.append(GeneralUtil.getI18n().getText("confluence.updatenotifier.releasenotes", Arrays.asList(version.getVersion(), version.getReleaseNotesURL())));
            }
            sb.append("</p>");
            sb.append("</div>");
            return sb.toString();
        }
        return "";
    }
}