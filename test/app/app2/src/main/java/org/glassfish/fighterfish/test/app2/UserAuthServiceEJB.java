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

package org.glassfish.fighterfish.test.app2;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class UserAuthServiceEJB
 */
@Stateless
@LocalBean
public class UserAuthServiceEJB 
{

    @PersistenceContext
    EntityManager em;

    @PostConstruct
    public void postConstruct() {
        System.out.println("UserAuthServiceEJB.postConstruct");
    }

    public boolean login(String name, String password)
    {
        System.out.println("UserAuthServiceEJBuser: logging in " + name);
        UserCredential uc = em.find(UserCredential.class, name);
        return (uc != null && uc.isMatchingPassword(password));
    }

    public boolean register(String name, String password)
    {
        System.out.println("UserAuthServiceEJB: registering " + name);
        UserCredential uc = new UserCredential(name, password);
        em.persist(uc);
        return true;
    }
}
