package top.iotequ.codegenerator.service.impl;
import top.iotequ.codegenerator.pojo.CgFormField;
import top.iotequ.codegenerator.dao.CgFormFieldDao;
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
import top.iotequ.util.*;
import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.codegenerator.service.impl.CgFormFieldService"})
@Service(value="cgFormFieldService")
public class CgCgFormFieldService extends CgService<CgFormField>  {
    private static final Logger log = LoggerFactory.getLogger(CgCgFormFieldService.class);
    @Autowired
    private CgFormFieldDao cgFormFieldDao;
    @Override
    public Class<CgFormField> getEntityClass() {
        return CgFormField.class;
    }
    @Override
    public IDaoService<CgFormField> getDaoService() {
        return cgFormFieldDao ;
    }
    @Override
    public IFlowService<CgFormField> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(CgFormField obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public CgFormField getDefaultObject(CgFormField cgFormField) throws IotequException {
        checkAvailable();
        if (cgFormField==null)  cgFormField=new CgFormField();
        else cgFormField.setId(null);
        return cgFormField;
    }
    @Override
    public void changeEmpty2Null(CgFormField cgFormField) {
        if (Objects.nonNull(cgFormField)) {
        }
    }
    @Override
    public void changeNull2Default(CgFormField cgFormField) {
        if (cgFormField.getId()==null) {
            cgFormField.setId(0);
        }
        if (cgFormField.getFormId()==null) {
            cgFormField.setFormId("");
        }
        if (cgFormField.getOrderNum()==null) {
            cgFormField.setOrderNum(0);
        }
        if (cgFormField.getEntityField()==null) {
            cgFormField.setEntityField("");
        }
        if (cgFormField.getWidth()==null) {
            cgFormField.setWidth(24);
        }
        if (cgFormField.getReadonly()==null) {
            cgFormField.setReadonly(Util.boolValue("0"));
        }
        if (cgFormField.getMustInput()==null) {
            cgFormField.setMustInput(Util.boolValue("0"));
        }
        if (cgFormField.getHidden()==null) {
            cgFormField.setHidden(Util.boolValue("0"));
        }
        if (cgFormField.getValidateAsTitle()==null) {
            cgFormField.setValidateAsTitle(Util.boolValue("0"));
        }
    }
}
