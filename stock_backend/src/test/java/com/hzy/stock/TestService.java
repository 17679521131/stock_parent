package com.hzy.stock;

import com.google.common.collect.Lists;
import com.hzy.stock.pojo.entity.SysPermission;
import com.hzy.stock.service.PermissionService;
import com.hzy.stock.service.StockService;
import com.hzy.stock.vo.resp.LoginRespPermission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import javax.jnlp.PersistenceService;
import java.util.ArrayList;
import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/22 00:01
 * @description :
 */
@SpringBootTest
public class TestService {

    @Autowired
    private PermissionService persionService;

    @Autowired
    private StockService stockService;

    public List<LoginRespPermission> getTree(List<SysPermission> permissions, long pid, boolean isOnlyMenuType) {
        //创建一个集合，用于存放便利好的树状菜单
        ArrayList<LoginRespPermission> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(permissions)) {
            return list;
        }
        for (SysPermission permission : permissions) {
            if (permission.getPid().equals(pid)) {
                if (permission.getType().intValue()!=3 || !isOnlyMenuType) {
                    LoginRespPermission respNodeVo = new LoginRespPermission();
                    respNodeVo.setId(permission.getId());
                    respNodeVo.setTitle(permission.getTitle());
                    respNodeVo.setIcon(permission.getIcon());
                    respNodeVo.setPath(permission.getUrl());
                    respNodeVo.setName(permission.getName());
                    respNodeVo.setChildren(getTree(permissions,permission.getId(),isOnlyMenuType));
                    list.add(respNodeVo);
                }
            }
        }
        return list;
    }

    @Test
    public void test(){
        List<SysPermission> permissionsByUserId = persionService.findPermissionsByUserId(1237361915165020161L);
        List<LoginRespPermission> tree = getTree(permissionsByUserId,0l,true);
        for (LoginRespPermission loginRespPermission : tree) {
            System.out.println(loginRespPermission);
            System.out.println("===============================");
        }
    }




    public List<LoginRespPermission> getTree1(List<SysPermission> permisssions,long pid,boolean b){
        ArrayList<LoginRespPermission> list = new ArrayList<>();
        if(permisssions != null){
            //返回一个空集合
            return list;
        }
        for (SysPermission permisssion : permisssions) {
            if(permisssion.getPid().equals(pid)){
                if(permisssion.getType() !=3 ||!b){
                    LoginRespPermission loginRespPermission = new LoginRespPermission();
                    loginRespPermission.setId(permisssion.getId());
                    loginRespPermission.setTitle(permisssion.getTitle());
                    loginRespPermission.setIcon(permisssion.getIcon());
                    loginRespPermission.setPath(permisssion.getUrl());
                    loginRespPermission.setName(permisssion.getName());
                    loginRespPermission.setChildren(getTree1(permisssions,permisssion.getId(),b));
                    list.add(loginRespPermission);
                }
            }
        }


        return null;
    }

    @Test
    public void test01(){
        stockService.stockScreenDKLine("600000");
    }



}
