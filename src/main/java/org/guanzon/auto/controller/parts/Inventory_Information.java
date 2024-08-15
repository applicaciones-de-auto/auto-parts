/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.controller.parts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.auto.model.parts.Model_Inventory_Information;
import org.guanzon.auto.validator.parts.ValidatorFactory;
import org.guanzon.auto.validator.parts.ValidatorInterface;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class Inventory_Information implements GRecord {

    GRider poGRider;
    boolean pbWtParent;
    int pnEditMode;
    String psBranchCd;
    String psRecdStat;

    Model_Inventory_Information poModel;
    JSONObject poJSON;
    
    CachedRowSet poVhclModel;

    public Inventory_Information(GRider foGRider, boolean fbWthParent, String fsBranchCd) {
        poGRider = foGRider;
        pbWtParent = fbWthParent;
        psBranchCd = fsBranchCd.isEmpty() ? foGRider.getBranchCode() : fsBranchCd;

        poModel = new Model_Inventory_Information(foGRider);
        pnEditMode = EditMode.UNKNOWN;
    }

    @Override
    public int getEditMode() {
        return pnEditMode;
    }

    @Override
    public void setRecordStatus(String fsValue) {
        psRecdStat = fsValue;
    }

    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        return poModel.setValue(fnCol, foData);
    }

    @Override
    public JSONObject setMaster(String fsCol, Object foData) {
        return poModel.setValue(fsCol, foData);
    }

    @Override
    public Object getMaster(int fnCol) {
        return poModel.getValue(fnCol);
    }

    @Override
    public Object getMaster(String fsCol) {
        return poModel.getValue(fsCol);
    }

    @Override
    public JSONObject newRecord() {
        poJSON = new JSONObject();
        try{
            pnEditMode = EditMode.ADDNEW;
            org.json.simple.JSONObject obj;

            poModel = new Model_Inventory_Information(poGRider);
            Connection loConn = null;
            loConn = setConnection();
            poModel.newRecord();
            poModel.setStockID(MiscUtil.getNextCode(poModel.getTable(), "sStockIDx", true, loConn, psBranchCd+"ST"));
            
            if (poModel == null){
                poJSON.put("result", "error");
                poJSON.put("message", "initialized new record failed.");
                return poJSON;
            }else{
                poJSON.put("result", "success");
                poJSON.put("message", "initialized new record.");
                pnEditMode = EditMode.ADDNEW;
            }
               
        }catch(NullPointerException e){
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        
        return poJSON;
    }
    
    private Connection setConnection(){
        Connection foConn;
        
        if (pbWtParent){
            foConn = (Connection) poGRider.getConnection();
            if (foConn == null) foConn = (Connection) poGRider.doConnect();
        }else foConn = (Connection) poGRider.doConnect();
        
        return foConn;
    }

    @Override
    public JSONObject openRecord(String fsValue) {
        pnEditMode = EditMode.READY;
        poJSON = new JSONObject();
        
        poModel = new Model_Inventory_Information(poGRider);
        poJSON = poModel.openRecord(fsValue);
        
        if("error".equals(poJSON.get("result"))){
            return poJSON;
        } 
        return poJSON;
    }

    @Override
    public JSONObject updateRecord() {
        poJSON = new JSONObject();
        if (pnEditMode != EditMode.READY && pnEditMode != EditMode.UPDATE){
            poJSON.put("result", "error");
            poJSON.put("message", "Invalid edit mode.");
            return poJSON;
        }
        pnEditMode = EditMode.UPDATE;
        poJSON.put("result", "success");
        poJSON.put("message", "Update mode success.");
        return poJSON;
    }

    @Override
    public JSONObject saveRecord(){
        
        ValidatorInterface validator = ValidatorFactory.make( ValidatorFactory.TYPE.Inventory_Information, poModel);
        validator.setGRider(poGRider);
        if (!validator.isEntryOkay()){
            poJSON.put("result", "error");
            poJSON.put("message", validator.getMessage());
            return poJSON;
        }
        
        poJSON = poModel.saveRecord();
        return poJSON;
    }

    @Override
    public JSONObject deleteRecord(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject deactivateRecord(String fsValue) {
        poJSON = new JSONObject();

        if (poModel.getEditMode() == EditMode.UPDATE) {
            poJSON = poModel.setActive(false);

            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }
            
            ValidatorInterface validator = ValidatorFactory.make( ValidatorFactory.TYPE.Inventory_Information, poModel);
            validator.setGRider(poGRider);
            if (!validator.isEntryOkay()){
                poJSON.put("result", "error");
                poJSON.put("message", validator.getMessage());
                return poJSON;
            }
            poJSON = poModel.saveRecord();
            if ("success".equals((String) poJSON.get("result"))) {
                poJSON.put("result", "success");
                poJSON.put("message", "Deactivation success.");
            } else {
                poJSON.put("result", "error");
                poJSON.put("message", "Deactivation failed.");
            }
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }
        return poJSON;
    }

    @Override
    public JSONObject activateRecord(String fsValue) {
        poJSON = new JSONObject();

        if (poModel.getEditMode() == EditMode.UPDATE) {
            poJSON = poModel.setActive(true);
            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }
            
            ValidatorInterface validator = ValidatorFactory.make( ValidatorFactory.TYPE.Inventory_Information, poModel);
            validator.setGRider(poGRider);
            if (!validator.isEntryOkay()){
                poJSON.put("result", "error");
                poJSON.put("message", validator.getMessage());
                return poJSON;
            }
            
            poJSON = poModel.saveRecord();
            if ("success".equals((String) poJSON.get("result"))) {
                poJSON.put("result", "success");
                poJSON.put("message", "Activation success.");
            } else {
                poJSON.put("result", "error");
                poJSON.put("message", "Activation failed.");
            }
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }
        return poJSON;
    }

    @Override
    public JSONObject searchRecord(String fsValue, boolean fbByActive) {
        JSONObject loJSON = new JSONObject();
        String lsSQL =    " SELECT "                                              
                        + "    a.sStockIDx "                                      
                        + "  , a.sBarCodex "                                      
                        + "  , a.sDescript "                                      
                        + "  , a.sBriefDsc "                                       
                        + "  , a.cRecdStat "                                        
                        + "  , b.sDescript AS sBrandNme "                         
                        + "  , c.sMeasurNm "                                      
                        + "  , d.sDescript AS sInvTypDs "
                        + "  , e.sDescript AS sCatgeDs1 "                         
                        + " FROM inventory a "                                    
                        + " LEFT JOIN brand b ON b.sBrandCde = a.sBrandCde    "   
                        + " LEFT JOIN measure c ON c.sMeasurID = a.sMeasurID  "   
                        + " LEFT JOIN inv_type d ON d.sInvTypCd = a.sInvTypCd "
                        + " LEFT JOIN inventory_category e ON e.sCategrCd = a.sCategCd1 "         ;
        
        if(fbByActive){
            lsSQL = MiscUtil.addCondition(lsSQL,  " sDescript LIKE " + SQLUtil.toSQL(fsValue + "%")
                                                    + " AND cRecdStat = '1' ");
        } else {
            lsSQL = MiscUtil.addCondition(lsSQL,  " sDescript LIKE " + SQLUtil.toSQL(fsValue + "%"));
        }
        
        System.out.println("SEARCH INVENTORY INFORMATION: " + lsSQL);
        loJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                "Stock ID»Barcode»Description",
                "sStockIDx»sBarCodex»sDescript",
                "sStockIDx»sBarCodex»sDescript",
                1);

        if (loJSON != null) {
        } else {
            loJSON = new JSONObject();
            loJSON.put("result", "error");
            loJSON.put("message", "No record loaded.");
            return loJSON;
        }
        
        return loJSON;
    }

    @Override
    public Model_Inventory_Information getModel() {
        return poModel;
    }
    
    public JSONObject loadVehicleModel(){
        JSONObject loJSON = new JSONObject();
        try {
            String lsSQL =    " SELECT "                                               
                            + "   a.sModelIDx "                                        
                            + " , a.sModelDsc "                                        
                            + " , a.sMakeIDxx "                                        
                            + " , a.cRecdStat "                                        
                            + " , b.sMakeDesc "                                        
                            + " FROM vehicle_model a "                                 
                            + " LEFT JOIN vehicle_make b ON b.sMakeIDxx = a.sMakeIDxx ";
            
            lsSQL = MiscUtil.addCondition(lsSQL,  " a.cRecdStat = '1' "
                                                   // + " AND a.sMakeIDxx = " + SQLUtil.toSQL(fsMakeID)
                                                    + " GROUP BY a.sModelDsc ORDER BY b.sMakeDesc, a.sModelDsc DESC ");
            
            
            System.out.println("LOAD VEHICLE MODEL "+ lsSQL);
            RowSetFactory factory = RowSetProvider.newFactory();
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            try {
                poVhclModel = factory.createCachedRowSet();
                poVhclModel.populate(loRS);
                MiscUtil.close(loRS);
                loJSON.put("result", "success");
                loJSON.put("message", "Vehicle Model load successfully.");
            } catch (SQLException e) {
                loJSON.put("result", "error");
                loJSON.put("message", e.getMessage());
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Inventory_Information.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loJSON;
    }
    
    public int getVehicleModelCount() throws SQLException{
        if (poVhclModel != null){
            poVhclModel.last();
            return poVhclModel.getRow();
        }else{
            return 0;
        }
    }
    
    public Object getVehicleModelDetail(int fnRow, int fnIndex) throws SQLException{
        if (fnIndex == 0) return null;
        
        poVhclModel.absolute(fnRow);
        return poVhclModel.getObject(fnIndex);
    }
    
    public Object getVehicleModelDetail(int fnRow, String fsIndex) throws SQLException{
        return getVehicleModelDetail(fnRow, MiscUtil.getColumnIndex(poVhclModel, fsIndex));
    }
    
}
