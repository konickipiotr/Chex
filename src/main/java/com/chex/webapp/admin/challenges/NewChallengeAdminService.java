package com.chex.webapp.admin.challenges;

import com.chex.files.FileService;
import com.chex.files.FileType;
import com.chex.lang.LanguageUtils;
import com.chex.modules.challenges.model.Challenge;
import com.chex.modules.challenges.model.ChallengeDescription;
import com.chex.modules.challenges.model.ChallengeName;
import com.chex.modules.challenges.model.ChallengePoint;
import com.chex.modules.challenges.repository.ChallengeDescriptionRepository;
import com.chex.modules.challenges.repository.ChallengeNameRepository;
import com.chex.modules.challenges.repository.ChallengePointRepository;
import com.chex.modules.challenges.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NewChallengeAdminService {

    private ChallengeRepository challengeRepository;
    private ChallengeNameRepository challengeNameRepository;
    private ChallengeDescriptionRepository challengeDescriptionRepository;
    private ChallengePointRepository challengePointRepository;
    private FileService fileService;

    @Autowired
    public NewChallengeAdminService(ChallengeRepository challengeRepository, ChallengeNameRepository challengeNameRepository, ChallengeDescriptionRepository challengeDescriptionRepository, ChallengePointRepository challengePointRepository, FileService fileService) {
        this.challengeRepository = challengeRepository;
        this.challengeNameRepository = challengeNameRepository;
        this.challengeDescriptionRepository = challengeDescriptionRepository;
        this.challengePointRepository = challengePointRepository;
        this.fileService = fileService;
    }

    public boolean isNameExist(ChallengeForm form){

        String name = new LanguageUtils(challengeNameRepository.findByEngOrPl(form.getNameEng(), form.getNamePl())).getName();
        if(name == null)
            return false;
        return true;
    }

    public void saveChallenge(ChallengeForm form, List<CheckPointForm> list){
        Challenge challenge = new Challenge();

        if(form.getImgTemp() != null && !form.getImgTemp().isEmpty() ){
            try {
                String filename = fileService.moveToFromTmpToWorkspace(form.getImgTemp(), FileType.CHALLENGE);
                challenge.setImg(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        challenge.setTimelimit(form);

        challenge.setLevel(form.getLevel());
        this.challengeRepository.save(challenge);

        List<ChallengePoint> points = new ArrayList<>();
        int seq = 0;
        for(CheckPointForm c : list){
            ChallengePoint challengePoint = preparePoint(c, challenge.getId());
            challengePoint.setSeq(seq);
            seq++;
            points.add(challengePoint);
        }
        this.challengePointRepository.saveAll(points);

        this.challengeNameRepository.save(new ChallengeName(challenge.getId(), form.getNamePl(), form.getNameEng()));
        this.challengeDescriptionRepository.save(new ChallengeDescription(challenge.getId(), form.getDescriptionPl(), form.getDescriptionEng()));
    }

    private ChallengePoint preparePoint(CheckPointForm f, Long challengeid){
        ChallengePoint point = new ChallengePoint();
        point.setChallengeid(challengeid);
        if(f.isPlace()){
            point.setPlaceid(f.getPlaceId());
        }else {
            point.setName(f.getName());
            point.setLatitude(f.getLatitude());
            point.setLongitude(f.getLongitude());
            point.setRadius(f.getRadius());
        }
        return point;
    }
}
