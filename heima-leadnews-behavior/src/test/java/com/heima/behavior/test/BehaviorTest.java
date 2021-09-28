package com.heima.behavior.test;

import com.heima.behavior.BehaviorJarApplication;
import com.heima.behavior.service.AppShowBehaviorService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.behavior.dtos.ShowBehaviorDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.threadlocal.AppThreadLocalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = BehaviorJarApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BehaviorTest {

    @Autowired
    private AppShowBehaviorService showBehaviorService;

    @Test
    public void testSava(){
        ApUser apUser = new ApUser();
        apUser.setId(21l);
        AppThreadLocalUtils.setUser(apUser);
        ShowBehaviorDto dto = new ShowBehaviorDto();
        List<ApArticle> articlesList = new ArrayList<>();
        ApArticle apArticle = new ApArticle();
        apArticle.setId(200);
        articlesList.add(apArticle);
        dto.setArticleIds(articlesList);
        showBehaviorService.saveShowBehavior(dto);
    }
}
