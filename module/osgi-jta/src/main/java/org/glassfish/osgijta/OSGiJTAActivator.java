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

package org.glassfish.osgijta;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.glassfish.osgijavaeebase.Extender;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Sanjeeb.Sahoo@Sun.COM
 */
public class OSGiJTAActivator implements BundleActivator {
    private ServiceRegistration extenderReg;

    public void start(BundleContext context) throws Exception {
        Extender extender = new JTAExtender(context);
        extenderReg = context.registerService(Extender.class.getName(), extender, null);
    }

    public void stop(BundleContext context) throws Exception {
        extenderReg.unregister();
    }
}
