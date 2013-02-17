/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package confluence.updatenotifier;

import com.atlassian.gzipfilter.org.apache.commons.lang.StringUtils;

/**
 *
 * @author oli
 */
public class ConfluenceVersion implements Comparable<ConfluenceVersion> {

    private String releaseNotesURL = null;
    private Integer major = 0;
    private Integer minor = 0;
    private Integer micro = 0;

    public ConfluenceVersion(String version) {

        if (version != null) {
            String[] split = version.trim().split("\\.");
            switch (split.length) {
                case 1:
                    major = parseNumber(split[0]);

                    break;
                case 2:
                    major = parseNumber(split[0]);
                    minor = parseNumber(split[1]);
                    break;
                case 3:
                    major = parseNumber(split[0]);
                    minor = parseNumber(split[1]);
                    micro = parseNumber(split[2]);
                    break;
            }
        }
    }

    /**
     * If the given number isn't valid, 0 returns
     *
     * @param string a String
     * @return If the given number isn't valid, 0 returns
     */
    private Integer parseNumber(String string) {
        if (StringUtils.isNumeric(string)) {
            return new Integer(string);
        } else {
            char c = string.charAt(0);
            if (c >= '0' && c <= '9') {
                return new Integer(c + "");
            }

        }
        return 0;
    }

    /**
     * <0 , if this is lower thn the argument @param o @return
     */
    @Override
    public int compareTo(ConfluenceVersion o) {
        if (o == null) {
            throw new NullPointerException();
        }
        int compareTo = this.major.compareTo(o.major);

        if (compareTo == 0) {
            compareTo = this.minor.compareTo(o.minor);
            if (compareTo == 0) {
                compareTo = this.micro.compareTo(o.micro);
            }
        }

        return compareTo;


    }

    /**
     * @return the version
     */
    public String getVersion() {
        return this.major + "." + this.minor + "." + this.micro;
    }

    public Boolean isOlderThan(ConfluenceVersion newVersion) {
        return this.compareTo(newVersion) < 0;
    }

    /**
     * @return the releaseNotesURL
     */
    public String getReleaseNotesURL() {
        return releaseNotesURL;
    }

    /**
     * @param releaseNotesURL the releaseNotesURL to set
     */
    public void setReleaseNotesURL(String releaseNotesURL) {
        this.releaseNotesURL = releaseNotesURL;
    }
}