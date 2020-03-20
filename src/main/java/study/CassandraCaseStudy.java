package study;

import arcatch.ArCatchAPI;
import arcatch.dsl.compartment.grammar.Compartment;
import arcatch.dsl.rule.common.Criticality;


public class CassandraCaseStudy {
	
	private static Compartment cassandra;	

	public static void main(String[] args) {		
		
		//https://github.com/apache/cassandra	
		String projetPath = "./Cassandra/";	
		
		//https://github.com/apache/cassandra/releases/tag/cassandra-3.11.6
		checkCassandra3116(projetPath + "Cassandra_3.11.6/src/java");
		
		//https://github.com/apache/cassandra/releases/tag/cassandra-3.10
		checkCassandra310(projetPath + "Cassandra_3.10/src/java");
				
		//https://github.com/apache/cassandra/releases/tag/cassandra-3.5
		checkCassandra35(projetPath + "Cassandra_3.5/src/java");
		
		//https://github.com/apache/cassandra/releases/tag/cassandra-3.0.0
		checkCassandra300(projetPath + "Cassandra_3.0.0/src/java");
		
		//https://github.com/apache/cassandra/releases/tag/cassandra-2.2.0
		checkCassandra220(projetPath + "Cassandra_2.2.0/src/java");
		
		//https://github.com/apache/cassandra/releases/tag/cassandra-2.1.0
		checkCassandra210(projetPath + "Cassandra_2.1.0/src/java");
		
		//https://github.com/apache/cassandra/releases/tag/cassandra-2.0.0
		checkCassandra200(projetPath + "Cassandra_2.0.0/src/java");
	
		//https://github.com/apache/cassandra/releases/tag/cassandra-1.2.0
		checkCassandra120(projetPath + "Cassandra_1.2.0/src/java");
		
		//https://github.com/apache/cassandra/releases/tag/cassandra-1.1.0
		checkCassandra110(projetPath + "Cassandra_1.1.0/src/java");
		
		//https://github.com/apache/cassandra/releases/tag/cassandra-1.0.0
		checkCassandra100(projetPath + "Cassandra_1.0.0/src/java");
		
	}

	private static void checkRules() {		
			
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoCA = 0.0")
				.build());	
		
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoCI = 0.0")
				.build());
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoCRN = 0.0")
				.build());		
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoEH = 0.0")
				.build());
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoDR = 0.0")
				.build());				
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoGH = 0.0")
				.build());
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoGS = 0.0")
				.build());
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoNPB = 0.0")
				.build());
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoOCA = 0.0")
				.build());		
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoOC = 0.0")
				.build());
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoSF = 0.0")
				.build());
		
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoSKS = 0.0")
				.build());						
		
		// Abstractness violation
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("(Ce / (Ce + Ca)) <= 0.3")
				.build());
		
		//Large Class according PMD tool: https://github.com/pmd/pmd/blob/master/pmd-java/src/main/java/net/sourceforge/pmd/lang/java/rule/design/DataClassRule.java
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("(((NoM + NoA) > 3) & (WMC < 31)) | (((NoM + NoA) > 5) & (WMC < 47))")
				.build());
				
		//TooManyFields according PMD tool: ttps://github.com/pmd/pmd/blob/master/pmd-java/src/main/java/net/sourceforge/pmd/lang/java/rule/design/TooManyFieldsRule.java
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoA > 15")
				.build());		
		
		//TooManyMethods according PMD tool: https://pmd.github.io/pmd-6.19.0/pmd_rules_java_design.html#toomanymethods
		ArCatchAPI.addRule(ArCatchAPI
				.ruleBuilder()
				.antiDrift()
				.criticality(Criticality.WARNING)
				.compartiment(cassandra)
				.constrainedTo("NoM > 10")
				.build());		
		
		ArCatchAPI.check();
	}
	
	private static void checkCassandra3116(String projectPath) {
		ArCatchAPI.setConfiguration(ArCatchAPI
				.configurationBuilder()
				.projectNameAndVersion("Cassandra", "3.11.6")
				.projectPath(projectPath)				
				.build());
		
		cassandra = ArCatchAPI
				.compartmentBuilder()
				.compartment("cassandra")
				.matching("org.apache.cassandra.*")
				.build();

		ArCatchAPI.addCompartment(cassandra);		
		
		checkRules();
	}
	
	private static void checkCassandra310(String projectPath) {
		ArCatchAPI.setConfiguration(ArCatchAPI
				.configurationBuilder()
				.projectNameAndVersion("Cassandra", "3.10")
				.projectPath(projectPath)				
				.build());
		
		cassandra = ArCatchAPI
				.compartmentBuilder()
				.compartment("cassandra")
				.matching("org.apache.cassandra.*")
				.build();

		ArCatchAPI.addCompartment(cassandra);		
		
		checkRules();
	}
	
	private static void checkCassandra35(String projectPath) {
		ArCatchAPI.setConfiguration(ArCatchAPI
				.configurationBuilder()
				.projectNameAndVersion("Cassandra", "3.5")
				.projectPath(projectPath)				
				.build());
		
		cassandra = ArCatchAPI
				.compartmentBuilder()
				.compartment("cassandra")
				.matching("org.apache.cassandra.*")
				.build();

		ArCatchAPI.addCompartment(cassandra);		
		
		checkRules();
	}
	
	private static void checkCassandra300(String projectPath) {
		ArCatchAPI.setConfiguration(ArCatchAPI
				.configurationBuilder()
				.projectNameAndVersion("Cassandra", "3.0.0")
				.projectPath(projectPath)				
				.build());
		
		cassandra = ArCatchAPI
				.compartmentBuilder()
				.compartment("cassandra")
				.matching("org.apache.cassandra.*")
				.build();

		ArCatchAPI.addCompartment(cassandra);		
		
		checkRules();
	}
	
	private static void checkCassandra220(String projectPath) {
		ArCatchAPI.setConfiguration(ArCatchAPI
				.configurationBuilder()
				.projectNameAndVersion("Cassandra", "2.2.0")
				.projectPath(projectPath)				
				.build());
		
		cassandra = ArCatchAPI
				.compartmentBuilder()
				.compartment("cassandra")
				.matching("org.apache.cassandra.*")
				.build();

		ArCatchAPI.addCompartment(cassandra);		
		
		checkRules();
	}
	
	private static void checkCassandra210(String projectPath) {
		ArCatchAPI.setConfiguration(ArCatchAPI
				.configurationBuilder()
				.projectNameAndVersion("Cassandra", "2.1.0")
				.projectPath(projectPath)				
				.build());
		
		cassandra = ArCatchAPI
				.compartmentBuilder()
				.compartment("cassandra")
				.matching("org.apache.cassandra.*")
				.build();

		ArCatchAPI.addCompartment(cassandra);		
		
		checkRules();
	}
	
	private static void checkCassandra200(String projectPath) {
		ArCatchAPI.setConfiguration(ArCatchAPI
				.configurationBuilder()
				.projectNameAndVersion("Cassandra", "2.0.0")
				.projectPath(projectPath)				
				.build());
		
		cassandra = ArCatchAPI
				.compartmentBuilder()
				.compartment("cassandra")
				.matching("org.apache.cassandra.*")
				.build();

		ArCatchAPI.addCompartment(cassandra);		
		
		checkRules();
	}
	
	private static void checkCassandra120(String projectPath) {
		ArCatchAPI.setConfiguration(ArCatchAPI
				.configurationBuilder()
				.projectNameAndVersion("Cassandra", "1.2.0")
				.projectPath(projectPath)				
				.build());
		
		cassandra = ArCatchAPI
				.compartmentBuilder()
				.compartment("cassandra")
				.matching("org.apache.cassandra.*")
				.build();

		ArCatchAPI.addCompartment(cassandra);		
		
		checkRules();
	}
	
	private static void checkCassandra110(String projectPath) {
		ArCatchAPI.setConfiguration(ArCatchAPI
				.configurationBuilder()
				.projectNameAndVersion("Cassandra", "1.1.0")
				.projectPath(projectPath)				
				.build());
		
		cassandra = ArCatchAPI
				.compartmentBuilder()
				.compartment("cassandra")
				.matching("org.apache.cassandra.*")
				.build();

		ArCatchAPI.addCompartment(cassandra);		
		
		checkRules();
	}
	
	private static void checkCassandra100(String projectPath) {
		ArCatchAPI.setConfiguration(ArCatchAPI
				.configurationBuilder()
				.projectNameAndVersion("Cassandra", "1.0.0")
				.projectPath(projectPath)				
				.build());
		
		cassandra = ArCatchAPI
				.compartmentBuilder()
				.compartment("cassandra")
				.matching("org.apache.cassandra.*")
				.build();

		ArCatchAPI.addCompartment(cassandra);		
		
		checkRules();
	}

	
}
