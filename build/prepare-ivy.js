(function() {
    if (ARI.classExists('org.apache.ivy.ant.IvyConfigure')) {
        ARI.echo('ivy installed');
        return;
    }

    var ivyVersion = '2.4.0-rc1';
    var jarFile = ARI.prop('ivy.jar.file');
    var ivyJarUrl = 'http://repo2.maven.org/maven2/org/apache/ivy/ivy/' + ivyVersion + '/ivy-' + ivyVersion + '.jar';

    ARI.antcall('mkdir', {
        dir: ARI.struct('file', ARI.prop('ivy.jar.dir'))
    });

    ARI.antcall('get', {
        src: ARI.struct('url', ivyJarUrl),
        dest: ARI.struct('file', jarFile)
    });
}());
