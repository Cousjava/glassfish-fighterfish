/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.osgiejb;

import org.glassfish.osgijavaeebase.OSGiDeploymentRequest;
import org.glassfish.osgijavaeebase.OSGiDeploymentContext;
import org.glassfish.api.ActionReport;
import org.glassfish.api.deployment.archive.ReadableArchive;
import org.glassfish.api.deployment.OpsParams;
import org.glassfish.server.ServerEnvironmentImpl;
import org.glassfish.internal.deployment.Deployment;
import org.osgi.framework.Bundle;

import java.util.logging.Logger;

import com.sun.enterprise.deploy.shared.ArchiveFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleReference;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * @author Sanjeeb.Sahoo@Sun.COM
 */
public class OSGiEJBDeploymentRequest extends OSGiDeploymentRequest {

    public OSGiEJBDeploymentRequest(Deployment deployer, ArchiveFactory archiveFactory, ServerEnvironmentImpl env, ActionReport reporter, Bundle b) {
        super(deployer, archiveFactory, env, reporter, b);
    }

    protected OSGiDeploymentContext getDeploymentContextImpl(ActionReport reporter, Logger logger, ReadableArchive archive, OpsParams opsParams, ServerEnvironmentImpl env, Bundle b) throws Exception {
        return new OSGiEJBDeploymentContext(reporter, logger, archive, opsParams, env, b);
    }

    @Override
    protected EJBBundle makeArchive() {
        Bundle host = getBundle();
        Bundle[] fragments = getPackageAdmin().getFragments(host);
        return new EJBBundle(fragments, host);
    }

    private PackageAdmin getPackageAdmin() {
        BundleContext ctx = BundleReference.class.cast(getClass().getClassLoader()).getBundle().getBundleContext();
        return PackageAdmin.class.cast(ctx.getService(ctx.getServiceReference(PackageAdmin.class.getName())));
    }

}
