(function() {

    [ {
        name: 'ivy-configure',
        classname: 'org.apache.ivy.ant.IvyConfigure'
    }, {
        name: 'ivy-resolve',
        classname: 'org.apache.ivy.ant.IvyResolve'
    }, {
        name: 'ivy-retrieve',
        classname: 'org.apache.ivy.ant.IvyRetrieve'
    } ].forEach(function(x) {

        ARI.taskdef(x.name, x.classname);
    });

    ARI.antcall('ivy-configure', {
        file: ARI.struct('file', 'ivysettings.xml')
    });

    ARI.antcall('ivy-resolve', {
        file: ARI.struct('file', 'ivy.xml'),
        haltonfailure: false
    });

    ARI.antcall('delete', {
        dir: ARI.struct('file', 'target/lib')
    });

    ARI.antcall('mkdir', {
        dir: ARI.struct('file', 'target/lib')
    });

    ARI.antcall('ivy-retrieve', {
        conf: '*',
        pattern: 'target/lib/default/[module]-[revision].[ext]'
    });
}());
