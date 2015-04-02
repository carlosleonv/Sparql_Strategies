package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.syntax.Element;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementService;
import javax.swing.JTextArea;

public class QueryParser {

    public static JTextArea texto = new JTextArea();

    public JTextArea getTexto() {
        return texto;
    }

    public void setTexto(JTextArea texto) {
        this.texto = texto;
    }

    
	public static SplitQuery splitQuery(String queryFile, String localEP) throws FileNotFoundException{
		//read query file
                texto.setText(null);
            
//		String queryString = new Scanner(new File(queryFile)).useDelimiter("\\Z").next();
                String queryString = queryFile;
                
		//transform into Jena Query object
		Query q = QueryFactory.create(queryString);
		
		System.out.println("______\nParsing Query\n______\n"+q);
                texto.append("______\nParsing Query\n______\n"+q);              
                texto.append(System.getProperty("line.separator"));   // Salto de línea 
		
		boolean localEPparsed = false;
		Element p1 = null,p2 = null; 
		String remoteEP= null;
		ElementGroup qBody = (ElementGroup) q.getQueryPattern();;
		for(Element e: qBody.getElements()){
			if( e instanceof ElementService){
				ElementService es = (ElementService) e;
				String epURI = es.getServiceURI();
				if(epURI.equals(localEP) & !localEPparsed){
					p1 = es.getElement();
					localEPparsed = true;
				}else{
					p2 = es.getElement();
					remoteEP= epURI;
				}
			}
		}
		SplitQuery sq = new SplitQuery(p1, localEP, p2, remoteEP);
		System.out.println(sq);
                // Muestra SplitQuery
                texto.append(sq.toString());              
                texto.append(System.getProperty("line.separator"));   // Salto de línea                 
		return sq;
	}

	public static String findJoinVar(List<Var> p1Vars, List<Var> p2Vars) {
		
			for(Var v: p1Vars){
				if(p2Vars.contains(v))
					return v.getName();
			}
			return "";
	}
}
