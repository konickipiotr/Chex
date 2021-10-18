package com.chex.user.model;

import com.chex.modules.challenges.CheckpointType;
import com.chex.modules.challenges.model.ChallengePoint;
import com.chex.user.ChallengeStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserChallengePoint{

    @Id
    @SequenceGenerator(name = "userchallenge_sequence", sequenceName = "userchallenge_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userchallenge_sequence")
    private Long id;
    private Long userchallengeid;
    private Long checkpointid;
    private Long challengeid;
    private Long userid;
    private LocalDateTime reachedat;
    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;
    @Enumerated(EnumType.STRING)
    private CheckpointType checkpointtype;

    public UserChallengePoint() {
        this.status = ChallengeStatus.NOTSTARTED;
    }

    public UserChallengePoint(Long userchallengeid, ChallengePoint point, Long userId) {
        this();
        this.userchallengeid = userchallengeid;
        this.checkpointid = point.getId();
        this.challengeid = point.getChallengeid();
        this.userid = userId;
        this.checkpointtype = point.getCheckpointtype();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserchallengeid() {
        return userchallengeid;
    }

    public void setUserchallengeid(Long userchallengeid) {
        this.userchallengeid = userchallengeid;
    }

    public Long getCheckpointid() {
        return checkpointid;
    }

    public void setCheckpointid(Long checkpointid) {
        this.checkpointid = checkpointid;
    }

    public Long getChallengeid() {
        return challengeid;
    }

    public void setChallengeid(Long challengeid) {
        this.challengeid = challengeid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public LocalDateTime getReachedat() {
        return reachedat;
    }

    public void setReachedat(LocalDateTime reachedat) {
        this.reachedat = reachedat;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }

    public CheckpointType getCheckpointtype() {
        return checkpointtype;
    }

    public void setCheckpointtype(CheckpointType checkpointtype) {
        this.checkpointtype = checkpointtype;
    }

}
