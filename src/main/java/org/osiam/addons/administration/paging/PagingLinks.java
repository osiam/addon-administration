package org.osiam.addons.administration.paging;

import java.util.List;

/**
 * This class represents a container with paging links.
 */
public class PagingLinks {
    private List<String> links;
    private String nextLink;
    private String prevLink;
    private String curLink;
    private String lastLink;
    private String firstLink;

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> numberLinks) {
        this.links = numberLinks;

        if (numberLinks != null && numberLinks.size() != 0) {
            this.lastLink = numberLinks.get(numberLinks.size() - 1);
            this.firstLink = numberLinks.get(0);
        }
    }

    public String getNextLink() {
        return nextLink;
    }

    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }

    public String getPrevLink() {
        return prevLink;
    }

    public void setPrevLink(String prevLink) {
        this.prevLink = prevLink;
    }

    public String getCurLink() {
        return curLink;
    }

    public void setCurLink(String curLink) {
        this.curLink = curLink;
    }

    public String getLastLink() {
        return lastLink;
    }

    public String getFirstLink() {
        return firstLink;
    }

}
