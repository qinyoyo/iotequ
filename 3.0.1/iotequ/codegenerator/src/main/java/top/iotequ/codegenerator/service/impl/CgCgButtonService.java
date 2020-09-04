package top.iotequ.codegenerator.service.impl;
import top.iotequ.codegenerator.pojo.CgButton;
import top.iotequ.codegenerator.dao.CgButtonDao;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.framework.service.impl.CgService;
import top.iotequ.framework.service.IDaoService;
import org.springframework.stereotype.Service;
import top.iotequ.framework.service.utils.DictionaryUtil;
import top.iotequ.framework.service.utils.UploadDownUtil;
import top.iotequ.framework.service.utils.QueryUtil;
import top.iotequ.framework.util.*;
import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.codegenerator.service.impl.CgButtonService"})
@Service(value="cgButtonService")
public class CgCgButtonService extends CgService<CgButton>  {
private static final Logger log = LoggerFactory.getLogger(CgCgButtonService.class);
    @Autowired
    private CgButtonDao cgButtonDao;
    @Override
    public Class<CgButton> getEntityClass() {
        return CgButton.class;
    }
    @Override
    public IDaoService<CgButton> getDaoService() {
        return cgButtonDao ;
    }
    @Override
    public IFlowService<CgButton> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(CgButton obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public CgButton getDefaultObject(CgButton cgButton) throws IotequException {
        checkAvailable();
        if (cgButton==null)  cgButton=new CgButton();
        else cgButton.setId(null);
        return cgButton;
    }
    @Override
    public void changeEmpty2Null(CgButton cgButton) {
        if (Objects.nonNull(cgButton)) {
        }
    }
}
