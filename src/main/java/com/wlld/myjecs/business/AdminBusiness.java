package com.wlld.myjecs.business;

import com.wlld.myjecs.Session.WlldSession;
import com.wlld.myjecs.config.Config;
import com.wlld.myjecs.config.ErrorCode;
import com.wlld.myjecs.mesEntity.AdminSentence;
import com.wlld.myjecs.mesEntity.AgreeAdmin;
import com.wlld.myjecs.mesEntity.MyAdmin;
import com.wlld.myjecs.mesEntity.Response;
import com.wlld.myjecs.mapper.SqlMapper;
import com.wlld.myjecs.sqlEntity.Admin;
import com.wlld.myjecs.sqlEntity.KeywordType;
import com.wlld.myjecs.sqlEntity.MyTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminBusiness {//训练管理业务
    @Autowired
    private SqlMapper sqlMapper;

    @Transactional
    public Response saveUser(MyAdmin myAdmin) {
        Response response = new Response();
        boolean admin = myAdmin.getAccount().equals(Config.adminAccount) && myAdmin.getPass_word().equals(Config.adminPassWord);
        if (sqlMapper.getAdminByAccount(myAdmin.getAccount()) == null && !admin) {
            sqlMapper.saveUser(myAdmin);
            response.setError(ErrorCode.OK.getError());
            response.setErrorMessage(ErrorCode.OK.getErrorMessage());
        } else {//该账号存在了
            response.setError(ErrorCode.invalidAccount.getError());
            response.setErrorMessage(ErrorCode.invalidAccount.getErrorMessage());
        }
        return response;
    }

    //管理员要获取当前分类，在申请账号 及每个分类的关键词信息，
    // 标注员获取全部当前分类 及每个分类的关键词信息
    public Response getInitMessage(HttpServletResponse request) {//获取初始化信息
        Response response = new Response();
        List<MyTree> myTrees = sqlMapper.getMyTree();//所有分类
        List<KeywordType> keywordTypeList = sqlMapper.getKeyWordType();//关键词类别信息
        int adminID = (int) WlldSession.getSESSION().getValue(request, "myID");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String myDate = simpleDateFormat.format(date);//今日时间
        if (adminID == -1) {//管理员
            List<Admin> admins = sqlMapper.getAdminToPass();//待通过标注员账号
            response.setAdmins(admins);
        } else {//标注员获取今日当前用户标注的语句
            List<AdminSentence> adminSentenceList = sqlMapper.getMySentenceByID(adminID, myDate);
            response.setAdminSentenceList(adminSentenceList);
        }
        response.setResponseType(Config.initMessage);
        response.setMyTrees(myTrees);
        response.setKeywordTypeList(keywordTypeList);
        return response;
    }


    public Response agree(HttpServletResponse request, AgreeAdmin agreeAdmin) {
        Response response = new Response();
        int adminID = agreeAdmin.getId();
        boolean agree = agreeAdmin.isAgree();
        if ((int) WlldSession.getSESSION().getValue(request, "myID") == -1) {
            Admin admin = sqlMapper.getAdminByID(adminID);
            if (admin != null) {
                if (agree) {//同意该用户
                    sqlMapper.agreeAdminPass(adminID);
                } else {//删除该用户
                    sqlMapper.deleteAdmin(adminID);
                }
                response.setResponseType(Config.agreeAdmin);
                response.setResultID(adminID);
                response.setError(ErrorCode.OK.getError());
                response.setErrorMessage(ErrorCode.OK.getErrorMessage());
            } else {//该用户不存在
                response.setError(ErrorCode.invalidAdminID.getError());
                response.setErrorMessage(ErrorCode.invalidAdminID.getErrorMessage());
            }
        } else {
            response.setError(ErrorCode.NotPower.getError());
            response.setErrorMessage(ErrorCode.NotPower.getErrorMessage());
        }
        return response;
    }

    public Response login(MyAdmin myAdmin, HttpServletResponse request) {//用户登录
        Response response = new Response();
        response.setResponseType(Config.loginRequest);
        if (myAdmin.getAccount().equals(Config.adminAccount) && myAdmin.getPass_word().equals(Config.adminPassWord)) {
            WlldSession.getSESSION().setValue(request, "myID", -1);
            response.setRole(1);
        } else {
            Admin admin = sqlMapper.getAdmin(myAdmin);
            if (admin != null) {
                response.setRole(2);
                WlldSession.getSESSION().setValue(request, "myID", admin.getId());
                response.setError(ErrorCode.OK.getError());
                response.setErrorMessage(ErrorCode.OK.getErrorMessage());
            } else {
                response.setError(ErrorCode.NotPower.getError());
                response.setErrorMessage(ErrorCode.NotPower.getErrorMessage());
            }
        }
        return response;
    }

}
