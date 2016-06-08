/**
 * 
 */
package zemoga.mgc_security_content_repository;


import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import zemoga.poc.security.jackrabbit.SecurityContentPOC;

import static org.junit.Assert.fail;

/**
 * @author juantorres
 *
 */
public class SecurityContentPOCTest {
	
	private static SecurityContentPOC service = null;

	
	@BeforeClass
	public static void init() {
		try {
			service = SecurityContentPOC.getInstance();
			System.out.println("Se Inicializó el repositorio!! ");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testRepositoryInit(){
		//Crear la estrucura de directiorios....
		Session session = service.getSession();
		if(session!=null){
			try {
				Node root = session.getRootNode();
				System.out.println("Root Node name : " + root.getName() +
						"Root Node identifier: " + root.getIdentifier() +
						"Root Node Path: " + root.getPath()
				);
			} catch (RepositoryException e) {
				e.printStackTrace();
				fail();
			}
		}
	}
	
	@Test
	public void testAddNodeToRepo(){
		try {
			// Store content 
	        Node hello = service.getSession().getRootNode().addNode("hello"); 
	        Node world = hello.addNode("world"); 
	        world.setProperty("message", "Hello, World!"); 
	        service.getSession().save(); 

	        // Retrieve content 
	        Node node = service.getSession().getRootNode().getNode("hello/world"); 
	        System.out.println(node.getPath()); 
	        System.out.println(node.getProperty("message").getString());
	        
	        //Remove content
	        Node root = service.getSession().getRootNode();
	        root.getNode("hello").remove();
	        service.getSession().save();
	        
	        //Show repository tree
	        NodeIterator it = root.getNodes();
	        while(it.hasNext()){
	        	Node current = it.nextNode();
	        	System.out.println("Current node: " + current.getName());
	        }
	        
		} catch (RepositoryException e) {
			fail();
			e.printStackTrace();
		} 
	}
	
	@Test
	public void removeHelloNodesTest(){
		
        try {
        	Node root = service.getSession().getRootNode();
        	//Show repository tree
	        NodeIterator it = root.getNodes();
	        while(it.hasNext()){
	        	Node current = it.nextNode();
	        	System.out.println("Current node: " + current.getName());
	        	if(current.getName().equals("hello")){
	        		current.remove();
		        	service.getSession().save();
		        	System.out.println("Node removed!");
	        	}
	        }     
			
		} catch (AccessDeniedException e) {
			fail();
			e.printStackTrace();
		} catch (VersionException e) {
			fail();
			e.printStackTrace();
		} catch (LockException e) {
			fail();
			e.printStackTrace();
		} catch (ConstraintViolationException e) {
			fail();
			e.printStackTrace();
		} catch (PathNotFoundException e) {
			fail();
			e.printStackTrace();
		} catch (RepositoryException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void closeSession(){
		service.getSession().logout();
	}

}
