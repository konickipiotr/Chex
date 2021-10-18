package com.chex.api.challenges;

import com.chex.modules.challenges.model.Challenge;
import com.chex.modules.challenges.repository.ChallengeRepository;
import com.chex.user.ChallengeStatus;
import com.chex.user.model.UserChallenge;
import com.chex.user.repository.UserChallengePointRepository;
import com.chex.user.repository.UserChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class ChallengeScheduledTasks {

    private final UserChallengeRepository userChallengeRepository;
    private final UserChallengePointRepository userChallengePointRepository;
    private final ChallengeRepository challengeRepository;

    @Autowired
    public ChallengeScheduledTasks(UserChallengeRepository userChallengeRepository, UserChallengePointRepository userChallengePointRepository, ChallengeRepository challengeRepository) {
        this.userChallengeRepository = userChallengeRepository;
        this.userChallengePointRepository = userChallengePointRepository;
        this.challengeRepository = challengeRepository;
    }

    @Scheduled(fixedRate = 4000, initialDelay = 2000)
    public void updateChallenges(){
        List<Challenge> all = this.challengeRepository.findAll();
        Map<Long, Long> collect = all.stream().collect(Collectors.toMap(Challenge::getId, Challenge::getTimelimit));

        List<UserChallenge> inProgress = this.userChallengeRepository.findInProgress();
        for(UserChallenge uch : inProgress){
            long limit = collect.get(uch.getChallengeid());
            long current = Duration.between(uch.getStartTime(), LocalDateTime.now()).toMillis();

            if(current >= limit){
                closeNotFinishedChallenges(uch);
            }
        }
    }

    private void closeNotFinishedChallenges(UserChallenge userChallenge){

        this.userChallengePointRepository.deleteByUserchallengeid(userChallenge.getId());
        userChallenge.setEndTime(LocalDateTime.now());
        userChallenge.setStatus(ChallengeStatus.FAILURE);
        this.userChallengeRepository.save(userChallenge);
    }

}
