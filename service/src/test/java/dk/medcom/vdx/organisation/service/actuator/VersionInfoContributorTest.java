package dk.medcom.vdx.organisation.service.actuator;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.info.Info;

import java.util.Arrays;
import java.util.Collections;

public class VersionInfoContributorTest {
    @Test
    public void testBuildVersionNoTag() {
        var commit = "abc";

        var builder = Mockito.mock(Info.Builder.class);

        var versionInfoContributor = new VersionInfoContributor(commit, Collections.emptyList());

        versionInfoContributor.contribute(builder);

        Mockito.verify(builder).withDetail("version", commit);
    }

    @Test
    public void testBuildVersionNoValidTags() {
        var commit = "abc";
        var tags = Arrays.asList("tag1", "tag2");
        var builder = Mockito.mock(Info.Builder.class);

        var versionInfoContributor = new VersionInfoContributor(commit, tags);

        versionInfoContributor.contribute(builder);

        Mockito.verify(builder).withDetail("version", commit);
    }

    @Test
    public void testBuildVersionValidTag() {
        var commit = "abc";
        var validTag = "v0.2.1";
        var tags = Arrays.asList("tag1", validTag, "tag2");
        var builder = Mockito.mock(Info.Builder.class);

        var versionInfoContributor = new VersionInfoContributor(commit, tags);

        versionInfoContributor.contribute(builder);

        Mockito.verify(builder).withDetail("version", validTag);
    }

    @Test
    public void testBuildVersionDevWhenNull() {
        var builder = Mockito.mock(Info.Builder.class);

        var versionInfoContributor = new VersionInfoContributor(null, null);

        versionInfoContributor.contribute(builder);

        Mockito.verify(builder).withDetail("version", "dev");
    }
}
