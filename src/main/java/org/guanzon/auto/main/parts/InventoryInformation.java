/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.main.parts;

import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.auto.controller.parameter.Parts_Bin_Master;
import org.guanzon.auto.controller.parameter.Parts_InventoryType_Master;
import org.guanzon.auto.controller.parameter.Parts_Section_Master;
import org.guanzon.auto.controller.parameter.Parts_Warehouse_Master;
import org.guanzon.auto.controller.parts.Inventory_Information;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class InventoryInformation implements GRecord{
    GRider poGRider;
    boolean pbWthParent;
    String psBranchCd;
    boolean pbWtParent;
    
    int pnEditMode;
    String psRecdStat;
    
    public JSONObject poJSON;
    
    Inventory_Information poController;
    Parts_InventoryType_Master poInvType;
    Parts_Bin_Master poBin;
    Parts_Section_Master poSection;
    Parts_Warehouse_Master poWarehouse;
    
    public InventoryInformation(GRider foAppDrver, boolean fbWtParent, String fsBranchCd){
        poController = new Inventory_Information(foAppDrver,fbWtParent,fsBranchCd);
        poInvType = new Parts_InventoryType_Master(foAppDrver,fbWtParent,fsBranchCd);
        poBin = new Parts_Bin_Master(foAppDrver,fbWtParent,fsBranchCd);
        poSection = new Parts_Section_Master(foAppDrver,fbWtParent,fsBranchCd);
        poWarehouse = new Parts_Warehouse_Master(foAppDrver,fbWtParent,fsBranchCd);
        
        poGRider = foAppDrver;
        pbWtParent = fbWtParent;
        psBranchCd = fsBranchCd.isEmpty() ? foAppDrver.getBranchCode() : fsBranchCd;
    }

    @Override
    public int getEditMode() {
        pnEditMode = poController.getEditMode();
        return pnEditMode;
    }

    @Override
    public void setRecordStatus(String fsValue) {
        psRecdStat = fsValue;
    }

    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        JSONObject obj = new JSONObject(); 
        obj.put("pnEditMode", pnEditMode);
        obj = poController.setMaster(fnCol, foData);
        return obj;
    }

    @Override
    public JSONObject setMaster(String fsCol, Object foData) {
        return poController.setMaster(fsCol, foData);
    }

    @Override
    public Object getMaster(int fnCol) {
        if(pnEditMode == EditMode.UNKNOWN)
            return null;
        else 
            return poController.getMaster(fnCol);
    }

    @Override
    public Object getMaster(String fsCol) {
        return poController.getMaster(fsCol);
    }

    @Override
    public JSONObject newRecord() {
        poJSON = new JSONObject();
        try{
            poJSON = poController.newRecord();
            
            if("success".equals(poJSON.get("result"))){
                pnEditMode = poController.getEditMode();
            } else {
                pnEditMode = EditMode.UNKNOWN;
            }
               
        }catch(NullPointerException e){
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
            pnEditMode = EditMode.UNKNOWN;
        }
        return poJSON;
    }

    @Override
    public JSONObject openRecord(String fsValue) {
        poJSON = new JSONObject();
        
        poJSON = poController.openRecord(fsValue);
        if("success".equals(poJSON.get("result"))){
            pnEditMode = poController.getEditMode();
        } else {
            pnEditMode = EditMode.UNKNOWN;
        }
        
        return poJSON;
    }

    @Override
    public JSONObject updateRecord(){
        poJSON = new JSONObject();  
        poJSON = poController.updateRecord();
        pnEditMode = poController.getEditMode();
        return poJSON;
    }

    @Override
    public JSONObject saveRecord() {
        poJSON =  poController.saveRecord();
        return poJSON;
    }

    @Override
    public JSONObject deleteRecord(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject deactivateRecord(String fsValue) {
        return poController.deactivateRecord(fsValue);
    }

    @Override
    public JSONObject activateRecord(String fsValue) {
        return poController.activateRecord(fsValue);
    }

    @Override
    public JSONObject searchRecord(String fsValue, boolean fbByActive) {
        poJSON = new JSONObject();  
        poJSON = poController.searchRecord(fsValue, fbByActive);
        if(!"error".equals(poJSON.get("result"))){
            poJSON = openRecord((String) poJSON.get("sStockIDx"));
        }
        return poJSON;
    }

    @Override
    public Inventory_Information getModel() {
        return poController;
    }
    
    public JSONObject searchInvType(String fsValue, boolean fbByActive) {
        poJSON = new JSONObject();  
        poJSON = poInvType.searchRecord(fsValue, fbByActive);
        if(!"error".equals(poJSON.get("result"))){
            poController.setMaster("sInvTypCd", poJSON.get("sInvTypCd"));
            poController.setMaster("sInvTypDs", poJSON.get("sDescript"));
        }
        
        return poJSON;
    }
    
    public JSONObject searchBin(String fsValue, boolean fbByActive) {
        poJSON = new JSONObject();  
        poJSON = poBin.searchRecord(fsValue, fbByActive);
        if(!"error".equals(poJSON.get("result"))){
            poController.setMaster("sBinIDxxx", poJSON.get("sBinIDxxx"));
            poController.setMaster("sBinNamex", poJSON.get("sBinNamex"));
        }
        
        return poJSON;
    }
    
    public JSONObject searchSection(String fsValue, boolean fbByActive) {
        poJSON = new JSONObject();  
        poJSON = poSection.searchRecord(fsValue, fbByActive);
        if(!"error".equals(poJSON.get("result"))){
            poController.setMaster("sSectnIDx", poJSON.get("sSectnIDx"));
            poController.setMaster("sSectnNme", poJSON.get("sSectnNme"));
        }
        
        return poJSON;
    }
    
    public JSONObject searchWarehouse(String fsValue, boolean fbByActive) {
        poJSON = new JSONObject();  
        poJSON = poWarehouse.searchRecord(fsValue, fbByActive);
        if(!"error".equals(poJSON.get("result"))){
            poController.setMaster("sWHouseID", poJSON.get("sWHouseID"));
            poController.setMaster("sWHouseNm", poJSON.get("sWHouseNm"));
        }
        
        return poJSON;
    }
}
