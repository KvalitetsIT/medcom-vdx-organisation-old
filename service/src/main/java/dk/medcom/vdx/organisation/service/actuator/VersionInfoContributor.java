package dk.medcom.vdx.organisation.service.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

import java.util.List;
import java.util.regex.Pattern;

public class VersionInfoContributor implements InfoContributor {
    private final String commit;
    private final List<String> versions;

    public VersionInfoContributor(String commit, List<String> versions) {
        this.commit = commit;
        this.versions = versions;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("version", buildVersion());
    }

    private String buildVersion() {
        if(versions == null && commit == null) {
            return "dev";
        }

        var pattern = Pattern.compile("^v[0-9]*\\.[0-9]*\\.[0-9]*");

        var optionalTag = versions.stream().filter(pattern.asPredicate()).findFirst();

        return optionalTag.orElse(commit);
    }
}
