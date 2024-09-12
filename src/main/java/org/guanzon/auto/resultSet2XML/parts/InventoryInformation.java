/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.resultSet2XML.parts;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;

/**
 *
 * @author Arsiela
 */
public class InventoryInformation {
    
    public static void main (String [] args){
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
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }

        System.out.println("Connected");
        
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Inventory_Information.xml");
        
        String lsSQL =    " SELECT "                                           
                        + "    a.sStockIDx "                                   
                        + "  , a.sBarCodex "                                   
                        + "  , a.sDescript "                                   
                        + "  , a.sBriefDsc "                                   
                        + "  , a.sAltBarCd "                                   
                        + "  , a.sCategCd1 "                                   
                        + "  , a.sCategCd2 "                                   
                        + "  , a.sCategCd3 "                                   
                        + "  , a.sCategCd4 "                                   
                        + "  , a.sBrandCde "                                   
                        + "  , a.sColorCde "                                   
                        + "  , a.sMeasurID "                                   
                        + "  , a.sInvTypCd "                                   
                        + "  , a.nUnitPrce "                                   
                        + "  , a.nSelPrice "                                   
                        + "  , a.nDiscLev1 "                                   
                        + "  , a.nDiscLev2 "                                   
                        + "  , a.nDiscLev3 "                                   
                        + "  , a.nDealrDsc "                                   
                        + "  , a.cComboInv "                                   
                        + "  , a.cWthPromo "                                   
                        + "  , a.cSerialze "                                   
                        + "  , a.cUnitType "                                   
                        + "  , a.cInvStatx "                                   
                        + "  , a.cGenuinex "                                   
                        + "  , a.cReplacex "                                   
                        + "  , a.sSupersed "                                   
                        + "  , a.sFileName "                                   
                        + "  , a.sTrimBCde "                                   
                        + "  , a.cRecdStat "                                   
                        + "  , a.sModified "                                   
                        + "  , a.dModified "                                   
                        + "  , b.sDescript AS sBrandNme "                      
                        + "  , c.sMeasurNm "                                   
                        + "  , d.sDescript AS sInvTypDs " 
                        + "  , e.sDescript AS sCatgeDs1 "                      
                        + " FROM inventory a "                                 
                        + " LEFT JOIN brand b ON b.sBrandCde = a.sBrandCde    "
                        + " LEFT JOIN measure c ON c.sMeasurID = a.sMeasurID  "
                        + " LEFT JOIN inv_type d ON d.sInvTypCd = a.sInvTypCd " 
                        + " LEFT JOIN inventory_category e ON e.sCategrCd = a.sCategCd1 "      
                        + " WHERE 0=1 ";
        
        //System.out.println(lsSQL);
        ResultSet loRS = instance.executeQuery(lsSQL);
        try {
            if (MiscUtil.resultSet2XML(instance, loRS, System.getProperty("sys.default.path.metadata"), "inventory", "")){
                System.out.println("ResultSet exported.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
