package com.ruizton.main.dao;

import com.ruizton.main.model.Fabout;
import com.ruizton.test.BaseTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/3/21 0021.
 */
public class FaboutDAOTest extends BaseTestCase {

    @Autowired
    FaboutDAO dao;

    @Test
    public void testFindById() throws Exception {
        assertNotNull(dao);
        Fabout fabout = dao.findById(2);
        assertNotNull(fabout);
    }
}