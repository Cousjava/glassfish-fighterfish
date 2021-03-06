/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.osgihttp;

import com.sun.enterprise.web.WebModule;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.Wrapper;
import org.apache.catalina.valves.ValveBase;
import org.osgi.service.http.HttpContext;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Since OSGi/HTTP service spec does not a notion of a unique path per http context,
 * we register all the OSGi servlets with the same {@link org.apache.catalina.Context} object.
 * We still need to demultiplex the OSGi servlet context which is separate for each {@link HttpContext}.
 * This valve helps in demultiplexing. It performs following tasks:
 * a) Sets the current WebModule in {@link InvocationContext}
 * b) Sets the current WebModule as the Context of the Request object.
 * c) Reset unsuccessfulSessionFind flag in Request object.
 * <p/>
 * See GLASSFISH-16764 for more details.
 *
 * @author Sanjeeb.Sahoo@Oracle.COM
 */
public class OSGiHttpContextValve extends ValveBase {
    private Logger logger = Logger.getLogger(getClass().getPackage().getName());

    public OSGiHttpContextValve() {
    }

    @Override
    public int invoke(Request request, Response response) throws IOException, ServletException {
        logger.entering("OSGiHttpContextValve", "invoke", new Object[]{request});
        Wrapper wrapper = request.getWrapper();
        if (wrapper instanceof OSGiServletWrapper) {
            final OSGiServletWrapper osgiWrapper = (OSGiServletWrapper) wrapper;
            final WebModule osgiWebModule = osgiWrapper.getWebModule();
            InvocationContextMgr.getInvocationContext().setWebModule(osgiWebModule);
            request.setContext(osgiWebModule);
            resetSessionFindAttr(request);
        }
        return INVOKE_NEXT;
    }

    /**
     * Since we switch the Session Manager midway in the request processing cycle, we have to
     * reset a flag called unsuccessfulSessionFind that's maintained inside the Request object.
     * If we don't reset it, no session corresponding to this OSGi Http Context will be found.
     *
     * @param request
     */
    private void resetSessionFindAttr(Request request) {
        if (request instanceof org.apache.catalina.connector.Request) {
            org.apache.catalina.connector.Request.class.cast(request).setUnsuccessfulSessionFind(false);
        } else {
            logger.logp(Level.FINE, "OSGiHttpContextValve", "resetSessionFindAttr", "request {0} is not of type {1} ",
                    new Object[]{request, org.apache.catalina.connector.Request.class});
        }
    }
}
