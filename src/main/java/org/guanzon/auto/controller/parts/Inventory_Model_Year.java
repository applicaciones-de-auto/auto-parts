/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.controller.parts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.auto.model.parts.Model_Inventory_Model_Year;
import org.guanzon.auto.validator.parts.ValidatorFactory;
import org.guanzon.auto.validator.parts.ValidatorInterface;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class Inventory_Model_Year {
    final String XML = "Model_Inventory_Model_Year.xml";
    GRider poGRider;
    String psBranchCd;
    boolean pbWtParent;
    
    int pnEditMode;
    String psMessagex;
    public JSONObject poJSON;
    
    ArrayList<Model_Inventory_Model_Year> paDetail;
    ArrayList<Model_Inventory_Model_Year> paRemDetail;
    
    public Inventory_Model_Year(GRider foAppDrver){
        poGRider = foAppDrver;
    }
    
    public int getEditMode() {
        return pnEditMode;
    }

    public Model_Inventory_Model_Year getVehicleModel(int fnIndex){
        if (fnIndex > paDetail.size() - 1 || fnIndex < 0) return null;
        
        return paDetail.get(fnIndex);
    }
    
    public JSONObject addDetail(String fsValue){
        if(paDetail == null){
           paDetail = new ArrayList<>();
        }
        
        poJSON = new JSONObject();
        if (paDetail.size()<=0){
            paDetail.add(new Model_Inventory_Model_Year(poGRider));
            paDetail.get(0).newRecord();
            
            paDetail.get(0).setValue("sStockIDx", "");
            poJSON.put("result", "success");
            poJSON.put("message", "Add record.");
        } else {
            paDetail.add(new Model_Inventory_Model_Year(poGRider));
            paDetail.get(paDetail.size()-1).newRecord();

            paDetail.get(paDetail.size()-1).setStockID("");
            poJSON.put("result", "success");
            poJSON.put("message", "Add record.");
        }
        return poJSON;
    }
    
    public JSONObject openDetail(String fsValue){
        paDetail = new ArrayList<>();
        paRemDetail = new ArrayList<>();
        poJSON = new JSONObject();
        String lsSQL =    " SELECT "                
                        + "    sStockIDx "        
                        + "  , sModelCde "       
                        + "  , nYearModl "        
                        + " FROM inventory_model_year " ;
        lsSQL = MiscUtil.addCondition(lsSQL, " sStockIDx = " + SQLUtil.toSQL(fsValue))
                                                + "  ORDER BY sModelCde, nYearModl ASC " ;
        System.out.println(lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        try {
            int lnctr = 0;
            if (MiscUtil.RecordCount(loRS) > 0) {
                while(loRS.next()){
                        paDetail.add(new Model_Inventory_Model_Year(poGRider));
                        paDetail.get(paDetail.size() - 1).openRecord(loRS.getString("sStockIDx"), loRS.getString("sModelCde"), loRS.getInt("nYearModl"));
                        
                        pnEditMode = EditMode.UPDATE;
                        lnctr++;
                        poJSON.put("result", "success");
                        poJSON.put("message", "Record loaded successfully.");
                    } 
                
            }else{
//                paDetail = new ArrayList<>();
//                addDetail(fsValue);
                poJSON.put("result", "error");
                poJSON.put("continue", true);
                poJSON.put("message", "No record selected.");
            }
            MiscUtil.close(loRS);
        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        return poJSON;
    }
    
    public JSONObject saveDetail(String fsValue){
        JSONObject obj = new JSONObject();
        
        int lnCtr;
        if(paRemDetail != null){
            int lnRemSize = paRemDetail.size() -1;
            if(lnRemSize >= 0){
                for(lnCtr = 0; lnCtr <= lnRemSize; lnCtr++){
                    obj = paRemDetail.get(lnCtr).deleteRecord();
                    if("error".equals((String) obj.get("result"))){
                        return obj;
                    }
                }
            }
        }
        
        if(paDetail == null){
            obj.put("result", "error");
            obj.put("continue", true);
            return obj;
        }
        
        int lnSize = paDetail.size() -1;
        if(lnSize < 0){
            obj.put("result", "error");
            obj.put("continue", true);
            return obj;
        }
        
        for (lnCtr = 0; lnCtr <= lnSize; lnCtr++){
            //Do not save the year when common is exist
            if(paDetail.get(lnCtr).getModelDsc().equals("COMMON")){
                obj.put("result", "error");
                obj.put("continue", true);
                return obj;
            }
            
            //if(lnCtr>0){
                if(paDetail.get(lnCtr).getYearModl() == 0){
                    continue; //skip, instead of removing the actual detail
//                    paDetail.remove(lnCtr);
//                    lnCtr++;
//                    if(lnCtr > lnSize){
//                        break;
//                    } 
                }
            //}
            
            paDetail.get(lnCtr).setStockID(fsValue);
            
            ValidatorInterface validator = ValidatorFactory.make(ValidatorFactory.TYPE.Inventory_Model_Year, paDetail.get(lnCtr));
            validator.setGRider(poGRider);
            if (!validator.isEntryOkay()){
                obj.put("result", "error");
                obj.put("message", validator.getMessage());
                return obj;
            }
            obj = paDetail.get(lnCtr).saveRecord();
        }    
        
        return obj;
    }
    
    public ArrayList<Model_Inventory_Model_Year> getDetailList(){
        if(paDetail == null){
           paDetail = new ArrayList<>();
        }
        return paDetail;
    }
    public void setDetailList(ArrayList<Model_Inventory_Model_Year> foObj){this.paDetail = foObj;}
    
    public void setDetail(int fnRow, int fnIndex, Object foValue){ paDetail.get(fnRow).setValue(fnIndex, foValue);}
    public void setDetail(int fnRow, String fsIndex, Object foValue){ paDetail.get(fnRow).setValue(fsIndex, foValue);}
    public Object getDetail(int fnRow, int fnIndex){return paDetail.get(fnRow).getValue(fnIndex);}
    public Object getDetail(int fnRow, String fsIndex){return paDetail.get(fnRow).getValue(fsIndex);}
    
    public Object removeDetail(int fnRow){
        JSONObject loJSON = new JSONObject();
        
        if(paDetail.get(fnRow).getStockID()!= null){
            if(!paDetail.get(fnRow).getStockID().isEmpty()){
                RemoveDetail(fnRow);
            }
        }
        
        paDetail.remove(fnRow);
        return loJSON;
    }
    
    private JSONObject RemoveDetail(Integer fnRow){
        
        if(paRemDetail == null){
           paRemDetail = new ArrayList<>();
        }
        
        poJSON = new JSONObject();
        if (paRemDetail.size()<=0){
            paRemDetail.add(new Model_Inventory_Model_Year(poGRider));
            paRemDetail.get(0).openRecord(paDetail.get(fnRow).getStockID(),paDetail.get(fnRow).getModelCde(),paDetail.get(fnRow).getYearModl());
            poJSON.put("result", "success");
            poJSON.put("message", "added to remove record.");
        } else {
            paRemDetail.add(new Model_Inventory_Model_Year(poGRider));
            paRemDetail.get(paRemDetail.size()-1).openRecord(paDetail.get(fnRow).getStockID(),paDetail.get(fnRow).getModelCde(),paDetail.get(fnRow).getYearModl());
            poJSON.put("result", "success");
            poJSON.put("message", "added to remove record.");
        }
        return poJSON;
    }
    
    
    
}
