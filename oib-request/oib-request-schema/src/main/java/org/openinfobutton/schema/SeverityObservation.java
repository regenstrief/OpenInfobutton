package org.openinfobutton.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.hl7.v3.REDSMT010001UVPerformer;
import org.hl7.v3.REDSMT010001UVSeverityObservation;
import org.openinfobutton.schemas.kb.Code;

public class SeverityObservation {
	
	private Code code;
	
	
	public SeverityObservation (String code, String codeSystem, 
			String displayName, String codeSystemName) {
		this.code = CodeUtility.getCode(code, codeSystem, displayName, codeSystemName);
	}
	
	public SeverityObservation (Code code) {
		this.code = code;
	}
	
	public SeverityObservation (REDSMT010001UVSeverityObservation severityObservation) {
		
		this.code = CodeUtility.getCode(severityObservation.getInterpretationCode());
	}
	
	public SeverityObservation () {
		
		this(CodeUtility.getCode());
	}
	
	public Code getCode() {
		
		return this.code;
	}
	
	public void setCode(Code code) {
		
		this.code = code;
	}
	
	public static JAXBElement<REDSMT010001UVSeverityObservation> getJAXBElement(SeverityObservation severityObservation) {
		REDSMT010001UVSeverityObservation element = new REDSMT010001UVSeverityObservation();
		element.setInterpretationCode(CodeUtility.getJAXBElement(severityObservation.getCode()));
		JAXBElement<REDSMT010001UVSeverityObservation> jaxBElement = new JAXBElement<REDSMT010001UVSeverityObservation>(new QName("urn:hl7-org:v3","severityObservation"), 
				REDSMT010001UVSeverityObservation.class, element);
		return jaxBElement;
	}
}
