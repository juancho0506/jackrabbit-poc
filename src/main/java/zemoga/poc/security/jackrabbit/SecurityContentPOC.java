package zemoga.poc.security.jackrabbit;

import java.util.Hashtable;
import java.util.Properties;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.jackrabbit.core.jndi.RegistryHelper;


/** 
* First hop example. Logs in to a content repository and prints a 
* status message. 
*/ 
public class SecurityContentPOC {
	
	/**************************
	 * CONSTANTES
	 *************************/
	
	public static int NUM_PARAMS = 3;
	
	private static SecurityContentPOC SERVICE_INSTANCE = null;
	
	/**************************
	 * ATRIBUTOS
	 **************************/
	/** El archivo de propiedades. **/
	Properties config = null;
	
	/** El repositorio a donde se va a conectar. **/
	private Repository rep = null;
	/** Workspace de trabajo para el cliente. **/
	private Workspace work = null;
	/** Usuario para conectarse al repositorio. **/
	private String user = "";
	/** Password para conectarse al repositorio. **/
	private String password = "";
	/** La session para trabajar con el repositorio. **/
	private Session session = null;
	/** Nodo raíz del workspace. **/
	private Node root = null;
	
	/** 
	 * Default constructor
	 */
	 public SecurityContentPOC() {
		super();
	}
	
	 /**
	  * Singleton instance.
	  * @return
	  * @throws Exception
	  */
	public static SecurityContentPOC getInstance() throws Exception{
		if (SERVICE_INSTANCE==null){
			SERVICE_INSTANCE = new SecurityContentPOC();
			SERVICE_INSTANCE.init();
		}
		return SERVICE_INSTANCE;
	}

	/** 
	 * The main entry point to init the repository. 
	 * @throws Exception if an error occurs 
	*/ 
    public void init() throws Exception {
        System.out.println( "Hello World!" );
        InitialContext ctx=null;
		rep = null;
		//A repository config file. 
		String configFile = "/Users/juantorres/Documents/desarrollo/tecnologias/JCR/jackrabbit/repository.xml";
		//Filesystem for Jackrabbit repository 
		String repHomeDir = "/Users/juantorres/Documents/desarrollo/tecnologias/JCR/jackrabbit";
		
		//Register the repository in JNDI
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.jackrabbit.core.jndi.provider.DummyInitialContextFactory");
		env.put(Context.PROVIDER_URL, "localhost");
		
		ctx = new InitialContext(env);
		try {
			RegistryHelper.registerRepository(ctx,"repo",configFile,repHomeDir,true);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		rep = (Repository) ctx.lookup("repo");
		user = "admin";
		password = "admin";
		session = rep.login(new SimpleCredentials(user, password.toCharArray()));
		work = session.getWorkspace();
		if(work!=null){
			System.out.println("Login succsessful, workspace : "+work.getName());
			root = session.getRootNode();
		}else{
			System.out.println("ERROR: No se pudo obtener el workspace..!!");
		}
		
        String user = session.getUserID(); 
        String name = rep.getDescriptor(Repository.REP_NAME_DESC); 
        System.out.println( 
        "Logged in as " + user + " to a " + name + " repository.");
        
    }

	public Repository getRep() {
		return rep;
	}

	public void setRep(Repository rep) {
		this.rep = rep;
	}

	public Workspace getWork() {
		return work;
	}

	public void setWork(Workspace work) {
		this.work = work;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
    
    
}
