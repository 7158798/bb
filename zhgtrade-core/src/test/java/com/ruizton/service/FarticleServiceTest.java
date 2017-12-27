package com.ruizton.service;

import com.ruizton.main.model.Farticle;
import com.ruizton.main.service.front.FrontOthersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by sunpeng on 2016/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class FarticleServiceTest {

    @Autowired
    private FrontOthersService frontOthersService;

    @Test
    public void testKeyword(){
        List<Farticle> list = frontOthersService.findArticleByKeyword(1, 10, "招股币");
        for (Farticle farticle : list) {
            System.out.println(farticle.getFid());
        }
    }
    @Test
    public void testKeywordsAnd(){
        List<Farticle> list = frontOthersService.findArticleByKeywordsAnd(1, 10, new String[]{"理财", "说明"});
        for (Farticle farticle : list) {
            System.out.println(farticle.getFid());
        }
    }
    @Test
    public void testKeywordsOr(){
        List<Farticle> list = frontOthersService.findArticleByKeywords(1, 10, new String[]{"理财", "说明"});
        for (Farticle farticle : list) {
            System.out.println(farticle.getFid());
        }
    }

}
