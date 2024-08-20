/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.main.parts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.auto.controller.parameter.Parts_Bin_Master;
import org.guanzon.auto.controller.parameter.Parts_Brand_Master;
import org.guanzon.auto.controller.parameter.Parts_InventoryCategory_Master;
import org.guanzon.auto.controller.parameter.Parts_InventoryType_Master;
import org.guanzon.auto.controller.parameter.Parts_Measure_Master;
import org.guanzon.auto.controller.parameter.Parts_Section_Master;
import org.guanzon.auto.controller.parameter.Parts_Warehouse_Master;
import org.guanzon.auto.controller.parts.Inventory_Information;
import org.guanzon.auto.controller.parts.Inventory_Model;
import org.guanzon.auto.controller.parts.Inventory_Model_Year;
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
    Inventory_Model poInventoryModel;
    Inventory_Model_Year poInventoryModelYear;
    
    Parts_InventoryType_Master poInvType;
    Parts_Bin_Master poBin;
    Parts_Section_Master poSection;
    Parts_Warehouse_Master poWarehouse;
    Parts_Brand_Master poBrand;
    Parts_InventoryCategory_Master poInvCategory;
    Parts_Measure_Master poMeasure;
    
    public InventoryInformation(GRider foAppDrver, boolean fbWtParent, String fsBranchCd){
        poController = new Inventory_Information(foAppDrver,fbWtParent,fsBranchCd);
        poInventoryModel = new Inventory_Model(foAppDrver);
        poInventoryModelYear = new Inventory_Model_Year(foAppDrver);
        
        poInvType = new Parts_InventoryType_Master(foAppDrver,fbWtParent,fsBranchCd);
        poBin = new Parts_Bin_Master(foAppDrver,fbWtParent,fsBranchCd);
        poSection = new Parts_Section_Master(foAppDrver,fbWtParent,fsBranchCd);
        poWarehouse = new Parts_Warehouse_Master(foAppDrver,fbWtParent,fsBranchCd);
        poBrand = new Parts_Brand_Master(foAppDrver,fbWtParent,fsBranchCd);
        poInvCategory = new Parts_InventoryCategory_Master(foAppDrver,fbWtParent,fsBranchCd);
        poMeasure = new Parts_Measure_Master(foAppDrver,fbWtParent,fsBranchCd);
        
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
        
        poJSON = poInventoryModel.openDetail(fsValue);
        if(!"success".equals(poJSON.get("result"))){
            pnEditMode = EditMode.UNKNOWN;
            return poJSON;
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
        poJSON = new JSONObject();  
        
        if (!pbWtParent) poGRider.beginTrans();
        
        poJSON =  poController.saveRecord();
        if("error".equalsIgnoreCase((String)checkData(poJSON).get("result"))){
            if (!pbWtParent) poGRider.rollbackTrans();
            return checkData(poJSON);
        }
        
        poJSON =  poInventoryModel.saveDetail((String) poController.getModel().getStockID());
        if("error".equalsIgnoreCase((String)checkData(poJSON).get("result"))){
            if (!pbWtParent) poGRider.rollbackTrans();
            return checkData(poJSON);
        }
        
        poJSON =  poInventoryModelYear.saveDetail((String) poController.getModel().getStockID());
        if("error".equalsIgnoreCase((String)checkData(poJSON).get("result"))){
            if (!pbWtParent) poGRider.rollbackTrans();
            return checkData(poJSON);
        }
        
        if (!pbWtParent) poGRider.commitTrans();
        
        return poJSON;
    }
    
    private JSONObject checkData(JSONObject joValue){
        if(pnEditMode == EditMode.ADDNEW ||pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE){
            if(joValue.containsKey("continue")){
                if(true == (boolean)joValue.get("continue")){
                    joValue.put("result", "success");
                    joValue.put("message", "Record saved successfully.");
                }
            }
        }
        return joValue;
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
    
    public ArrayList getInventoryModelList(){return poInventoryModel.getDetailList();}
    private void setInventoryModelList(ArrayList foObj){this.poInventoryModel.setDetailList(foObj);}
    
    private void setInventoryModel(int fnRow, int fnIndex, Object foValue){ poInventoryModel.setDetail(fnRow, fnIndex, foValue);}
    private void setInventoryModel(int fnRow, String fsIndex, Object foValue){ poInventoryModel.setDetail(fnRow, fsIndex, foValue);}
    public Object getInventoryModel(int fnRow, int fnIndex){return poInventoryModel.getDetail(fnRow, fnIndex);}
    public Object getInventoryModel(int fnRow, String fsIndex){return poInventoryModel.getDetail(fnRow, fsIndex);}
    
    private Object addInventoryModel(){ return poInventoryModel.addDetail(poController.getModel().getStockID());}
    private Object removeInventoryModel(int fnRow){ return poInventoryModel.removeDetail(fnRow);}
    
    public ArrayList getInventoryModelYearList(){return poInventoryModelYear.getDetailList();}
    private void setInventoryModelYearList(ArrayList foObj){this.poInventoryModelYear.setDetailList(foObj);}
    
    private void setInventoryModelYear(int fnRow, int fnIndex, Object foValue){ poInventoryModelYear.setDetail(fnRow, fnIndex, foValue);}
    private void setInventoryModelYear(int fnRow, String fsIndex, Object foValue){ poInventoryModelYear.setDetail(fnRow, fsIndex, foValue);}
    public Object getInventoryModelYear(int fnRow, int fnIndex){return poInventoryModelYear.getDetail(fnRow, fnIndex);}
    public Object getInventoryModelYear(int fnRow, String fsIndex){return poInventoryModelYear.getDetail(fnRow, fsIndex);}
    
    private Object addInventoryModelYear(){ return poInventoryModelYear.addDetail(poController.getModel().getStockID());}
    private Object removeInventoryModelYear(int fnRow){ return poInventoryModelYear.removeDetail(fnRow);}
    
    public JSONObject loadModel() {
        return poController.loadVehicleModel();
    }
    
    public int getModelCount() throws SQLException{
        return poController.getVehicleModelCount();
    }
    
    public Object getModelDetail(int fnRow, int fnIndex) throws SQLException{
        return poController.getVehicleModelDetail(fnRow, fnIndex);
    }
    
    public Object getModelDetail(int fnRow, String fsIndex) throws SQLException{
        return poController.getVehicleModelDetail(fnRow, fsIndex);
    }
    
    /**
    ***Adds an inventory model or inventory model year to the database.
    *@param fsModelID The code of the inventory model to be added.
    *@param fsModelDesc The description of the inventory model to be added.
    *@param fsMakeID The code of the make of the inventory model make to be added.
    *@param fsMakeDesc The description of the make of the inventory model to be added.
    *@param fnYear The year of the model to be added (applicable if fbIsModelOnly is false).
    *@param fbIsModelOnly {@code true} if only the inventory model is to be added, {@code false} if the model year is to be added.
    *@return {@code true} if the inventory model or model year was successfully added, {@code false} otherwise.
    */
    public JSONObject addInvModel_Year(String fsModelID, String fsModelDesc, String fsMakeID, String fsMakeDesc, Integer fnYear, boolean fbIsModelOnly){
        JSONObject loJSON = new JSONObject();
        int lnCtr;
        if (fsModelID.equals("")){
            loJSON.put("result", "error");
            loJSON.put("message", "Please select Vehicle Model.");
            return loJSON;
        }
        
        for (lnCtr = 0; lnCtr <= getInventoryModelList().size()-1; lnCtr++){
            if (fsModelDesc.equals("COMMON") && getInventoryModel(lnCtr,"sModelDsc").equals("COMMON") ){
                loJSON.put("result", "error");
                loJSON.put("message", "COMMON already exist.");
                return loJSON;
            }
        }
        
        if (fsModelDesc.equals("COMMON") && (getInventoryModelList().size()-1 >= 0 || getInventoryModelYearList().size()-1 >= 0)){
            loJSON.put("result", "error");
            loJSON.put("message",  "Cannot add a common vehicle model when other models exist in Inventory Model/Year.");
            return loJSON;
        }
        
        if (fsModelDesc.equals("COMMON") && !fnYear.equals(0) ){
            loJSON.put("result", "error");
            loJSON.put("message",  "Cannot add a common vehicle model with Year Model.");
            return loJSON;
        }
        
        if(fbIsModelOnly){
            //Validate Model
            for (lnCtr = 0; lnCtr <= getInventoryModelList().size()-1; lnCtr++){

                if (fsModelID.equals(getInventoryModel(lnCtr,"sModelCde"))){
                    loJSON.put("result", "error");
                    loJSON.put("message",  "Skipping, Failed to add Vehicle Model " + fsModelDesc + " already exist.");
                    return loJSON;
                }  
                
                if (getInventoryModel(lnCtr,"sModelDsc").equals("COMMON")){
                    loJSON.put("result", "error");
                    loJSON.put("message",  "You cannot add other vehicle model");
                    return loJSON;
                }
            }
            
            for (lnCtr = 0; lnCtr <= getInventoryModelYearList().size()-1; lnCtr++){
                if (fsModelID.equals(getInventoryModelYear(lnCtr,"sModelCde"))
                        && (Integer) getInventoryModelYear(lnCtr,"nYearModl") != 0 ){
                    loJSON.put("result", "error");
                    loJSON.put("message",  "Skipping, Failed to add Vehicle Model " + fsModelDesc + " already exist with Year Model.");
                    return loJSON;
                }
            }
            
            addInventoryModel();
            setInventoryModel(getInventoryModelList().size()-1,"sModelCde",fsModelID);
            setInventoryModel(getInventoryModelList().size()-1,"sModelDsc",fsModelDesc);
            setInventoryModel(getInventoryModelList().size()-1,"sMakeIDxx",fsMakeID);
            setInventoryModel(getInventoryModelList().size()-1,"sMakeDesc",fsMakeDesc);
            
            addInventoryModelYear();
            setInventoryModelYear(getInventoryModelYearList().size()-1,"sModelCde",fsModelID);
            setInventoryModelYear(getInventoryModelYearList().size()-1,"sModelDsc",fsModelDesc);
            setInventoryModelYear(getInventoryModelYearList().size()-1,"sMakeIDxx",fsMakeID);
            setInventoryModelYear(getInventoryModelYearList().size()-1,"sMakeDesc",fsMakeDesc);
            setInventoryModelYear(getInventoryModelYearList().size()-1,"nYearModl",0);
        } else {
            //Validate Model Year
            for (lnCtr = 0; lnCtr <= getInventoryModelYearList().size()-1; lnCtr++){
                if (fsModelID.equals(getInventoryModelYear(lnCtr,"sModelCde"))
                        && fnYear.equals(getInventoryModelYear(lnCtr,"nYearModl"))){
                    loJSON.put("result", "error");
                    loJSON.put("message",  "Skipping, Failed to add Vehicle Model " + fsModelDesc + " - " + String.valueOf(fnYear) + " already exist.");
                    return loJSON;
                }
                
                if (fsModelID.equals(getInventoryModelYear(lnCtr,"sModelCde"))
                        && (Integer) getInventoryModelYear(lnCtr,"nYearModl") == 0 ){
                    loJSON.put("result", "error");
                    loJSON.put("message",  "Skipping, Failed to add Vehicle Model " + fsModelDesc + " already exist without Year Model.");
                    return loJSON;
                }
                
                if (getInventoryModelYear(lnCtr,"sModelDsc").equals("COMMON")){
                    loJSON.put("result", "error");
                    loJSON.put("message", "You cannot add other vehicle model");
                    return loJSON;
                }
            }
            
//            for (lnCtr = 0; lnCtr <= getInventoryModelList().size()-1; lnCtr++){
//                if (fsModelID.equals(getInventoryModel(lnCtr,"sModelCde"))){
//                    loJSON.put("result", "error");
//                    loJSON.put("message",  "Skipping, Failed to add Year Model " + fsModelDesc + " - " + String.valueOf(fnYear) + " already exist without Year Model.");
//                    return loJSON;
//                }
//                
//                if (getInventoryModel(lnCtr,"sModelDsc").equals("COMMON")){
//                    loJSON.put("result", "error");
//                    loJSON.put("message", "You cannot add other vehicle model");
//                    return loJSON;
//                }
//            }
            
            addInventoryModelYear();
            setInventoryModelYear(getInventoryModelYearList().size()-1,"sModelCde",fsModelID);
            setInventoryModelYear(getInventoryModelYearList().size()-1,"sModelDsc",fsModelDesc);
            setInventoryModelYear(getInventoryModelYearList().size()-1,"sMakeIDxx",fsMakeID);
            setInventoryModelYear(getInventoryModelYearList().size()-1,"sMakeDesc",fsMakeDesc);
            setInventoryModelYear(getInventoryModelYearList().size()-1,"nYearModl",fnYear);
            
            boolean lbCheckExistMdl = false;
            for (lnCtr = 0; lnCtr <= getInventoryModelList().size()-1; lnCtr++){
                if (fsModelID.equals(getInventoryModel(lnCtr,"sModelCde"))){
                    lbCheckExistMdl = true;
                    break;
                }
                
            }
            
            //Add inventory model
            if(!lbCheckExistMdl){
                addInventoryModel();
                setInventoryModel(getInventoryModelList().size()-1,"sModelCde",fsModelID);
                setInventoryModel(getInventoryModelList().size()-1,"sModelDsc",fsModelDesc);
                setInventoryModel(getInventoryModelList().size()-1,"sMakeIDxx",fsMakeID);
                setInventoryModel(getInventoryModelList().size()-1,"sMakeDesc",fsMakeDesc);
            }
        }
        
        return loJSON;
    }
    
    /**
    * Removes selected inventory models and model years from the cache and database.
    * 
    * @param fsInvModel Inventory Model Code: to be Removed
    * @param fnRowModelYr Inventory Model Year: An array for inventory model year per model code
    * @return {@code true} if the removal was successful, {@code false} otherwise.
    */
    public JSONObject removeInvModel_Year(String fsInvModel, Integer fnRowModelYr[]) {
        JSONObject loJSON = new JSONObject();
        if (getInventoryModelYearList().size()-1 < 0 && getInventoryModelList().size()-1 <= 0) {
            loJSON.put("result", "error");
            loJSON.put("message", "No Vehicle Model / Year to delete.");
            return loJSON;
        }
        
        int fnYear = 0;
        boolean lbExistMdlCd = false;
        if(fnRowModelYr.length != 0){
            //Remove Inventory Model Year
            for (int lnRow : fnRowModelYr){
                fnYear = fnRowModelYr[lnRow];
                for(int lnCtr = 0; lnCtr <= getInventoryModelYearList().size()-1; lnCtr++ ){
                    if(String.valueOf(getInventoryModelYear(lnCtr,"sModelCde")).equals(fsInvModel)){
                        if(String.valueOf(getInventoryModelYear(lnCtr,"nYearModl")).equals(String.valueOf(fnYear))){
                            removeInventoryModelYear(lnCtr);
                            break;
                        }
                    }
                }
            }
            
            //Check if Model Code is Still exist in Inventory Model Year, if not exist the remove it in inventory model
            for(int lnCtr = 0; lnCtr <= getInventoryModelYearList().size()-1; lnCtr++ ){
                if(String.valueOf(getInventoryModelYear(lnCtr,"sModelCde")).equals(fsInvModel)){
                    lbExistMdlCd = true;
                    break;
                }
            }
        }
        
        //Remove Inventory Model
        if(!lbExistMdlCd){
            for(int lnCtr = 0; lnCtr <= getInventoryModelList().size()-1; lnCtr++ ){
                if(String.valueOf(getInventoryModel(lnCtr,"sModelCde")).equals(fsInvModel)){
                    removeInventoryModel(lnCtr);
                    break;
                }
            }
        }

        loJSON.put("result", "success");
        loJSON.put("message", "Inventory Model/Year successfully removed.");
        return loJSON;
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
    
    public JSONObject searchBrand(String fsValue, boolean fbByActive) {
        poJSON = new JSONObject();  
        poJSON = poBrand.searchRecord(fsValue, fbByActive);
        if(!"error".equals(poJSON.get("result"))){
            poController.setMaster("sBrandCde", poJSON.get("sBrandCde"));
            poController.setMaster("sBrandNme", poJSON.get("sDescript"));
        }
        
        return poJSON;
    }
    
    public JSONObject searchInvCategory(String fsValue, boolean fbByActive) {
        poJSON = new JSONObject();  
        poJSON = poInvCategory.searchRecord(fsValue, fbByActive);
        if(!"error".equals(poJSON.get("result"))){
            poController.setMaster("sCategCd1", poJSON.get("sCategCd1"));
            poController.setMaster("sCatgeDs1", poJSON.get("sDescript"));
        }
        
        return poJSON;
    }
    
    public JSONObject searchMeasure(String fsValue, boolean fbByActive) {
        poJSON = new JSONObject();  
        poJSON = poMeasure.searchRecord(fsValue, fbByActive);
        if(!"error".equals(poJSON.get("result"))){
            poController.setMaster("sMeasurID", poJSON.get("sMeasurID"));
            poController.setMaster("sMeasurNm", poJSON.get("sMeasurNm"));
        }
        
        return poJSON;
    }
    
//    public JSONObject searchBin(String fsValue, boolean fbByActive) {
//        poJSON = new JSONObject();  
//        poJSON = poBin.searchRecord(fsValue, fbByActive);
//        if(!"error".equals(poJSON.get("result"))){
//            poController.setMaster("sBinIDxxx", poJSON.get("sBinIDxxx"));
//            poController.setMaster("sBinNamex", poJSON.get("sBinNamex"));
//        }
//        
//        return poJSON;
//    }
//    
//    public JSONObject searchSection(String fsValue, boolean fbByActive) {
//        poJSON = new JSONObject();  
//        poJSON = poSection.searchRecord(fsValue, fbByActive);
//        if(!"error".equals(poJSON.get("result"))){
//            poController.setMaster("sSectnIDx", poJSON.get("sSectnIDx"));
//            poController.setMaster("sSectnNme", poJSON.get("sSectnNme"));
//        }
//        
//        return poJSON;
//    }
//    
//    public JSONObject searchWarehouse(String fsValue, boolean fbByActive) {
//        poJSON = new JSONObject();  
//        poJSON = poWarehouse.searchRecord(fsValue, fbByActive);
//        if(!"error".equals(poJSON.get("result"))){
//            poController.setMaster("sWHouseID", poJSON.get("sWHouseID"));
//            poController.setMaster("sWHouseNm", poJSON.get("sWHouseNm"));
//        }
//        
//        return poJSON;
//    }
}
