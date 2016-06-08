/**
 * 
 */
package zemoga.poc.security.jackrabbit;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

/**
 * @author juantorres
 *
 */
public class POCMain {
	
	private static SecurityContentPOC contentService;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			contentService = SecurityContentPOC.getInstance();
			Node root = contentService.getSession().getRootNode();
        	//Show repository tree
	        NodeIterator it = root.getNodes();
	        while(it.hasNext()){
	        	Node current = it.nextNode();
	        	System.out.println("Current node: " + current.getName());
	        	if(current.getName().equals("mod_entitlement") || current.getName().equals("mod_data_point_labels")){
	        		current.remove();
	        		contentService.getSession().save();
		        	System.out.println("Node removed!");
	        	}
	        }
	        //Generate Schema	        
	        generatePOCContentStructure();
	        
	        Node testNode = root.getNode("mod_data_point_labels/page_data_point_label_rules/page_rule_set_detail/page_rule_setup_tab/page_localized_rules_tab");
	        System.out.println("Type of node: " + testNode.getProperty("type").getString());
	        testNode.getNode("restriction");
	        System.out.println(testNode.getPath()); 
	        System.out.println(testNode.getNode("restriction").getProperty("STG").getString());
	        
		} catch (Exception e) {
			System.err.println("Error to load the content repository.");
			e.printStackTrace();
		}

	}
	
	public static void generatePOCContentStructure() throws RepositoryException{
		System.out.println("Generating Content repository schema ......");
		// Store content 
		
		//Entitlement module
        Node entitlementModule = contentService .getSession().getRootNode().addNode("mod_entitlement"); 
        entitlementModule.setProperty("type", "module");
        Node usersPage = entitlementModule.addNode("page_users"); 
        usersPage.setProperty("type", "page");
        Node usersGroupPage = entitlementModule.addNode("page_users_groups");
        usersGroupPage.setProperty("type", "page"); 
        contentService.getSession().save(); 
        
        //DataPoint Labels module
        Node dataPointLabelsModule = contentService .getSession().getRootNode().addNode("mod_data_point_labels"); 
        dataPointLabelsModule.setProperty("type", "module");
        Node localizationModule = dataPointLabelsModule.addNode("mod_localization"); 
        localizationModule.setProperty("type", "module");
        Node dataPointLabelRulesPage = dataPointLabelsModule.addNode("page_data_point_label_rules");
        dataPointLabelRulesPage.setProperty("type", "page");
        contentService.getSession().save();
        
        //Childs for DataPoint Rule Set Detail Page
        Node ruleSetDetailPage = dataPointLabelRulesPage.addNode("page_rule_set_detail");
        ruleSetDetailPage.setProperty("type", "page");
        Node ruleSetupTab = ruleSetDetailPage.addNode("page_rule_setup_tab");
        ruleSetupTab.setProperty("type", "page");
        ruleSetDetailPage.addNode("page_rule_Assigment Tab").setProperty("type", "page");
        
        Node ruleSetAddPage = dataPointLabelRulesPage.addNode("page_rule_set_add");
        ruleSetAddPage.setProperty("type", "page");
        contentService.getSession().save();
        
        //Rule Set Detail componentes and pages
        Node inputSystemDefaultRule = ruleSetDetailPage.addNode("input_system_default_rule");
        inputSystemDefaultRule.setProperty("type", "component");
        Node inputSystemDefaultRuleRestricc = inputSystemDefaultRule.addNode("restriction");
        inputSystemDefaultRuleRestricc.setProperty("STG", "READ");
        contentService.getSession().save();
        
        //Childs for Rule Setup Tab Page
        Node localizedRulesTabPage = ruleSetupTab.addNode("page_localized_rules_tab");
        localizedRulesTabPage.setProperty("type", "page");
        Node customRulesTabPage = ruleSetupTab.addNode("customRulesTab");
        customRulesTabPage.setProperty("type", "page");
        
        Node localizedRulesRestricc = localizedRulesTabPage.addNode("restriction");
        localizedRulesRestricc.setProperty("STG", "READ");
        
        Node customRulesRestricc = customRulesTabPage.addNode("restriction");
        customRulesRestricc.setProperty("STG", "HIDE");
        contentService.getSession().save();
        
        //Components for localized rules tab page
        Node selectFilterLocalizedRulesLanguage = localizedRulesTabPage.addNode("select_filter_localized_rules_language");
        selectFilterLocalizedRulesLanguage.setProperty("type", "component");
        Node selectFilterLocalizedRulesLanguageRestricc = selectFilterLocalizedRulesLanguage.addNode("restriction");
        selectFilterLocalizedRulesLanguageRestricc.setProperty("STG", "disabled");
        
        Node selectFilterLocalizedRulesRegion = localizedRulesTabPage.addNode("select_filter_localized_rules_region");
        selectFilterLocalizedRulesRegion.setProperty("type", "component");
        Node selectFilterLocalizedRulesRegionRestricc = selectFilterLocalizedRulesRegion.addNode("restriction");
        selectFilterLocalizedRulesRegionRestricc.setProperty("STG", "disabled");
        contentService.getSession().save();
        
        //Add button page restriction
        Node addNewButtonDataPointRule = ruleSetDetailPage.addNode("buttonAddNewId");
        addNewButtonDataPointRule.setProperty("type", "compoment");
        Node addNewButtonDataPointRuleRestricc = addNewButtonDataPointRule.addNode("restriction");
        addNewButtonDataPointRuleRestricc.setProperty("STG", "disabled");
        contentService.getSession().save();
        
        
        System.out.println("Content repository schema generated successfully!!");
        
	}

}
