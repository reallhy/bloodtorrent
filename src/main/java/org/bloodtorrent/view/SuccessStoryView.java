package org.bloodtorrent.view;

import lombok.Getter;
import lombok.Setter;
import org.bloodtorrent.dto.SuccessStory;
import org.bloodtorrent.dto.User;

import java.util.List;

@SuppressWarnings("PMD.UnusedPrivateField")
public class SuccessStoryView extends CommonView {

    private static final String PATH = "/ftl/successStory.ftl";
    private static final String PATH_SUCCESS_STORY_LIST = "/ftl/successStoryList.ftl";
    private static final String PATH_SUCCESS_STORY_VIEW = "/ftl/successStoryEditor.ftl";

    private boolean savedSuccessFlag;
    @Getter @Setter
    private boolean editSuccessFlag;
    @Getter
	private SuccessStory successStory;

    @Getter @Setter
    private List<SuccessStory> successStoryList;

    @Getter @Setter
    private User user;

    public void setSavedSuccessFlag(Boolean flag) {
        savedSuccessFlag = flag.booleanValue();
    }

    public boolean getSavedSuccessFlag() {
        return savedSuccessFlag;
    }

    public SuccessStoryView(SuccessStory successStory) {
		super(PATH);
        String description = successStory.getDescription();
        successStory.setDescription(description.replaceAll(System.getProperty("line.separator"), "<br/>"));
        this.successStory = successStory;
	}

    public SuccessStoryView(List<SuccessStory> successStoryList) {
        super(PATH_SUCCESS_STORY_LIST);
        this.successStoryList = successStoryList;
    }

    public SuccessStoryView() {
        super(PATH_SUCCESS_STORY_VIEW);
    }
}

