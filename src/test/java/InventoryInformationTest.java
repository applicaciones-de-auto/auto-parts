
import java.sql.SQLException;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.parts.InventoryInformation;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arsiela
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InventoryInformationTest {
    
    static InventoryInformation model;
    JSONObject json;
    boolean result;
    
    public InventoryInformationTest(){}
    
    @BeforeClass
    public static void setUpClass() {   
        
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Maven_Systems";
        }
        else{
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        GRider instance = new GRider("gRider");
        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getMessage() + instance.getErrMsg());
            System.exit(1);
        }
        
        System.out.println("Connected");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        
        
        JSONObject json;
        
        System.out.println("sBranch code = " + instance.getBranchCode());
        model = new InventoryInformation(instance,false, instance.getBranchCode());
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }
    
    /**
     * COMMENTED TESTING TO CLEAN AND BUILD PROPERLY
     * WHEN YOU WANT TO CHECK KINDLY UNCOMMENT THE TESTING CASES (@Test).
     * ARSIELA 
     */
    
//    @Test
//    public void test01NewRecord() throws SQLException{
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------NEW RECORD--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.newRecord();
//        if ("success".equals((String) json.get("result"))){
//            json = model.setMaster("sBarCodex","BARCODE1");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("sDescript","HONDA CAR COVER");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("sBriefDsc","CAR COVER");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("sAltBarCd","ALTBRCD01");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("sCategCd1","M00124000001");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("sBrandCde","M0010001");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("sMeasurID","M001001");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("sInvTypCd","0001");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("nUnitPrce",0.00);
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("nSelPrice",0.00);
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("nDealrDsc",0.00);
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("cComboInv","0");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("cWthPromo","0");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("cSerialze","0");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("cUnitType","0");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("cInvStatx","0");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("cGenuinex","0");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("cReplacex","0");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("sSupersed","");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("sFileName","");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.setMaster("sTrimBCde","BARCODE1");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            model.loadModel();
//            
//            model.addInventoryModel();
//            System.out.println("inventory model size = " + model.getInventoryModelList().size());
//            for(int lnctr = 0; lnctr <= model.getInventoryModelList().size()-1; lnctr++){
//                model.setInventoryModel(lnctr, "sModelCde", "M001MD000001");
//            }
//            
//            model.addInventoryModelYear();
//            System.out.println("inventory model year size = " + model.getInventoryModelYearList().size());
//            for(int lnctr = 0; lnctr <= model.getInventoryModelYearList().size()-1; lnctr++){
//                model.setInventoryModelYear(lnctr, "sModelCde", "M001MD000001");
//                model.setInventoryModelYear(lnctr, "nYearModl", 2000+lnctr);
//            }
//            
//                 
//        } else {
//            System.err.println("result = " + (String) json.get("result"));
//            fail((String) json.get("message"));
//        }
//        
//    }
//    
//    @Test
//    public void test01NewRecordSave(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------NEW RECORD SAVING--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.saveRecord();
//        System.err.println((String) json.get("message"));
//        
//        if (!"success".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            result = false;
//        } else {
//            System.out.println((String) json.get("message"));
//            result = true;
//        }
//        assertTrue(result);
//        //assertFalse(result);
//    }
//    
    @Test
    public void test02OpenRecord(){
        System.out.println("--------------------------------------------------------------------");
        System.out.println("------------------------------RETRIEVAL--------------------------------------");
        System.out.println("--------------------------------------------------------------------");
        
        json = model.openRecord("M001ST240001");
        
        if (!"success".equals((String) json.get("result"))){
            result = false;
        } else {
            System.out.println("--------------------------------------------------------------------");
            System.out.println("INVENTORY INFORMATION");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("sStockIDx  :  " + model.getMaster("sStockIDx"));
            System.out.println("sBarCodex  :  " + model.getMaster("sBarCodex"));
            System.out.println("sDescript  :  " + model.getMaster("sDescript"));
            System.out.println("sBriefDsc  :  " + model.getMaster("sBriefDsc"));
            System.out.println("sAltBarCd  :  " + model.getMaster("sAltBarCd"));
            System.out.println("sCategCd1  :  " + model.getMaster("sCategCd1"));
            System.out.println("sCategCd2  :  " + model.getMaster("sCategCd2"));
            System.out.println("sCategCd3  :  " + model.getMaster("sCategCd3"));
            System.out.println("sCategCd4  :  " + model.getMaster("sCategCd4"));
            System.out.println("sBrandCde  :  " + model.getMaster("sBrandCde"));
            System.out.println("sModelCde  :  " + model.getMaster("sModelCde"));
            System.out.println("sColorCde  :  " + model.getMaster("sColorCde"));
            System.out.println("sMeasurID  :  " + model.getMaster("sMeasurID"));
            System.out.println("sInvTypCd  :  " + model.getMaster("sInvTypCd"));
            System.out.println("nUnitPrce  :  " + model.getMaster("nUnitPrce"));
            System.out.println("nSelPrice  :  " + model.getMaster("nSelPrice"));
            System.out.println("nDiscLev1  :  " + model.getMaster("nDiscLev1"));
            System.out.println("sMeasurID  :  " + model.getMaster("sMeasurID"));
            System.out.println("nDiscLev2  :  " + model.getMaster("nDiscLev2"));
            System.out.println("nDiscLev3  :  " + model.getMaster("nDiscLev3"));
            System.out.println("nDealrDsc  :  " + model.getMaster("nDealrDsc"));
            System.out.println("cComboInv  :  " + model.getMaster("cComboInv"));
            System.out.println("cWthPromo  :  " + model.getMaster("cWthPromo"));
            System.out.println("cSerialze  :  " + model.getMaster("cSerialze"));
            System.out.println("cUnitType  :  " + model.getMaster("cUnitType"));
            System.out.println("cInvStatx  :  " + model.getMaster("cInvStatx"));
            System.out.println("cGenuinex  :  " + model.getMaster("cGenuinex"));
            System.out.println("cReplacex  :  " + model.getMaster("cReplacex"));
            System.out.println("sSupersed  :  " + model.getMaster("sSupersed"));
            System.out.println("sFileName  :  " + model.getMaster("sFileName"));
            System.out.println("sTrimBCde  :  " + model.getMaster("sTrimBCde"));
            System.out.println("cRecdStat  :  " + model.getMaster("cRecdStat"));
            System.out.println("sModified  :  " + model.getMaster("sModified"));
            System.out.println("dModified  :  " + model.getMaster("dModified"));
            System.out.println("sBrandNme  :  " + model.getMaster("sBrandNme"));
            System.out.println("sMeasurNm  :  " + model.getMaster("sMeasurNm"));
            System.out.println("sInvTypDs  :  " + model.getMaster("sInvTypDs"));
            
            
            System.out.println("--------------------------------------------------------------------");
            System.out.println("INVENTORY MODEL");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("inventory model size = " + model.getInventoryModelList().size());
            for(int lnctr = 0; lnctr <= model.getInventoryModelList().size()-1; lnctr++){
                System.out.println("sStockIDx  :  " + model.getInventoryModel(lnctr, "sStockIDx"));
                System.out.println("nEntryNox  :  " + model.getInventoryModel(lnctr, "nEntryNox"));
                System.out.println("sModelCde  :  " + model.getInventoryModel(lnctr, "sModelCde"));
                System.out.println("sModelDsc  :  " + model.getInventoryModel(lnctr, "sModelDsc"));
                System.out.println("sMakeIDxx  :  " + model.getInventoryModel(lnctr, "sMakeIDxx"));
                System.out.println("sMakeDesc  :  " + model.getInventoryModel(lnctr, "sMakeDesc"));
            }
            
            
            System.out.println("--------------------------------------------------------------------");
            System.out.println("INVENTORY MODEL YEAR");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("inventory model year size = " + model.getInventoryModelYearList().size());
            for(int lnctr = 0; lnctr <= model.getInventoryModelYearList().size()-1; lnctr++){
                System.out.println("sStockIDx  :  " +model.getInventoryModelYear(lnctr, "sStockIDx"));
                System.out.println("nYearModl  :  " +model.getInventoryModelYear(lnctr, "nYearModl"));
                System.out.println("sModelCde  :  " +model.getInventoryModelYear(lnctr, "sModelCde"));
                System.out.println("sModelDsc  :  " +model.getInventoryModelYear(lnctr, "sModelDsc"));
                System.out.println("sMakeIDxx  :  " +model.getInventoryModelYear(lnctr, "sMakeIDxx"));
                System.out.println("sMakeDesc  :  " +model.getInventoryModelYear(lnctr, "sMakeDesc"));
            }
            
            
            result = true;
        }
        assertTrue(result);
        //assertFalse(result);
    }
    
//    @Test
//    public void test03UpdateRecord(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------UPDATE RECORD--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.updateRecord();
//        System.err.println((String) json.get("message"));
//        if ("error".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            result = false;
//        } else {
//            result = true;
//        }
//        
//        json = model.setMaster("sInvTypCd","0001");
//        if ("error".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            System.exit(1);
//        }
//        assertTrue(result);
//        //assertFalse(result);
//    }
//    
//    @Test
//    public void test03UpdateRecordSave(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------UPDATE RECORD SAVING--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.saveRecord();
//        System.err.println((String) json.get("message"));
//        
//        if (!"success".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            result = false;
//        } else {
//            System.out.println((String) json.get("message"));
//            result = true;
//        }
//        assertTrue(result);
//        //assertFalse(result);
//    }
//    
//    @Test
//    public void test04DeactivateRecord(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------DEACTIVATE RECORD--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.deactivateRecord("M0010001");
//        System.err.println((String) json.get("message"));
//        
//        if (!"success".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            result = false;
//        } else {
//            System.out.println((String) json.get("message"));
//            result = true;
//        }
//        
//        assertTrue(result);
//        //assertFalse(result);
//    }
    
//    @Test
//    public void test04ActivateRecord(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------ACTIVATE RECORD--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.activateRecord("M0010001");
//        System.err.println((String) json.get("message"));
//        
//        if (!"success".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            result = false;
//        } else {
//            System.out.println((String) json.get("message"));
//            result = true;
//        }
//        
//        assertTrue(result);
//        //assertFalse(result);
//    }
}
