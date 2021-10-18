package com.chex.api.challenges;


import com.chex.modules.challenges.view.ChallengeShortView;
import com.chex.modules.challenges.view.ChallengeView;

import java.util.List;

public class UserChallengeResponse {

    private ReturnCode returnCode;
    private List<ChallengeShortView> available;
    private List<ChallengeView> inProgress;
    private List<ChallengeView> completed;


    public UserChallengeResponse(ReturnCode returnCode) {
        this.returnCode = returnCode;
    }

    public UserChallengeResponse() {
        this.returnCode = ReturnCode.OK;
    }

    public ReturnCode getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(ReturnCode returnCode) {
        this.returnCode = returnCode;
    }

    public List<ChallengeShortView> getAvailable() {
        return available;
    }

    public void setAvailable(List<ChallengeShortView> available) {
        this.available = available;
    }

    public List<ChallengeView> getInProgress() {
        return inProgress;
    }

    public void setInProgress(List<ChallengeView> inProgress) {
        this.inProgress = inProgress;
    }

    public List<ChallengeView> getCompleted() {
        return completed;
    }

    public void setCompleted(List<ChallengeView> completed) {
        this.completed = completed;
    }
}
