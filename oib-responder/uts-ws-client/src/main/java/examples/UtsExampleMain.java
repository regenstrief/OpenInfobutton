package examples;

import com.sun.org.apache.bcel.internal.classfile.Code;
import gov.nih.nlm.uts.webservice.content.AtomClusterRelationDTO;
import gov.nih.nlm.uts.webservice.content.Psf;
import gov.nih.nlm.uts.webservice.content.SourceAtomClusterDTO;
import gov.nih.nlm.uts.webservice.content.UtsFault_Exception;
import gov.nih.nlm.uts.webservice.content.UtsWsContentController;
import gov.nih.nlm.uts.webservice.content.UtsWsContentControllerImplService;
import gov.nih.nlm.uts.webservice.security.UtsWsSecurityController;
import gov.nih.nlm.uts.webservice.security.UtsWsSecurityControllerImplService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rick
 */
public class UtsExampleMain {

    private String username = null;
    private String password = null;
    private String umlsRelease = null;
    private String serviceName = null;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUmlsRelease(String umlsRelease) {
        this.umlsRelease = umlsRelease;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    private void getIcd9Children(int level, String code) {

        UtsWsContentController utsContentService = (new UtsWsContentControllerImplService()).getUtsWsContentControllerImplPort();

        List<AtomClusterRelationDTO> myAtomClusterRelations = new ArrayList<AtomClusterRelationDTO>();
        Psf myPsf = new Psf();
        myPsf.getIncludedRelationLabels().add("PAR");

        try {
            myAtomClusterRelations =
                    utsContentService.getSourceDescriptorSourceDescriptorRelations(getSecurityTicket(), umlsRelease, code, "ICD9CM", myPsf);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < myAtomClusterRelations.size(); i++) {

            AtomClusterRelationDTO myAtomClusterRelationDTO = myAtomClusterRelations.get(i);
            String otherAtomClusterUi = myAtomClusterRelationDTO.getRelatedAtomCluster().getUi();
            String otherAtomClusterName = myAtomClusterRelationDTO.getRelatedAtomCluster().getDefaultPreferredName();
            String otherAtomClusterRel = myAtomClusterRelationDTO.getRelationLabel();
            
            System.out.println(level + ":" + otherAtomClusterRel + ":" + code + " hasChild " + otherAtomClusterUi + " " + otherAtomClusterName);
            
            getIcd9Children(level + 1, otherAtomClusterUi);
        }

    }

        private void getSnomedChildren(int level, String code) {

        UtsWsContentController utsContentService = (new UtsWsContentControllerImplService()).getUtsWsContentControllerImplPort();
        SourceAtomClusterDTO conceptAtomCluster = null;
        List<AtomClusterRelationDTO> myRelations = null;
        Psf myPsf = new Psf();
        myPsf.getIncludedRelationLabels().add("PAR");

        try {

            conceptAtomCluster = utsContentService.getCode(getSecurityTicket(), umlsRelease, code, "SNOMEDCT");

            myRelations =
                    utsContentService.getSourceConceptSourceConceptRelations(getSecurityTicket(), umlsRelease, code, "SNOMEDCT", myPsf);

        } catch (UtsFault_Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < myRelations.size(); i++) {

            AtomClusterRelationDTO myRelation = myRelations.get(i);
            String cuiCode = myRelation.getRelatedAtomCluster().getUi();
            String cuiPreferredName = myRelation.getRelatedAtomCluster().getDefaultPreferredName();
            
            System.out.println("Snomed CUI = " + cuiCode + " meaning " + cuiPreferredName );

            getSnomedChildren(level + 1, cuiCode );
        }

    }

    private String getSecurityTicket() {

        String singleUseTicket = null;
        UtsWsSecurityController securityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();

        try {
            String ticketGrantingTicket = securityService.getProxyGrantTicket(username, password);
            singleUseTicket = securityService.getProxyTicket(ticketGrantingTicket, serviceName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return singleUseTicket;
    }

    public static void main(String[] args) {

        UtsExampleMain utsWebService = new UtsExampleMain();
        utsWebService.setUsername(args[0]);
        utsWebService.setPassword(args[1]);
        utsWebService.setUmlsRelease("2012AB");
        utsWebService.setServiceName("http://umlsks.nlm.nih.gov");

        System.out.println("============== START ====================");

        utsWebService.getIcd9Children(1, "482");
        utsWebService.getSnomedChildren(1,"47505003");

        System.out.println("=============== END =====================");

    }
}
