package com.chex.user.model;

import com.chex.modules.challenges.model.Challenge;
import com.chex.user.ChallengeStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserChallenge implements Comparable<UserChallenge> {

    @Id
    @SequenceGenerator(name = "userchallenge_sequence", sequenceName = "userchallenge_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userchallenge_sequence")
    private Long id;
    private Long userid;
    private Long challengeid;
    private int attemptnum;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    public UserChallenge() {
        this.status = ChallengeStatus.NOTSTARTED;
    }

    public UserChallenge(Challenge challenge, Long userId) {
        this();
        this.userid = userId;
        this.challengeid = challenge.getId();
        this.attemptnum = 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getChallengeid() {
        return challengeid;
    }

    public void setChallengeid(Long challengeid) {
        this.challengeid = challengeid;
    }

    public int getAttemptnum() {
        return attemptnum;
    }

    public void setAttemptnum(int attemptnum) {
        this.attemptnum = attemptnum;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(UserChallenge o) {
        return this.attemptnum >= o.attemptnum ? -1 : 1;
    }
}
