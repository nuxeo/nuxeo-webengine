/*
 * (C) Copyright 2006-2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bstefanescu
 *
 * $Id$
 */

package org.nuxeo.ecm.platform.site.scripting;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.platform.rendering.api.RenderingEngine;
import org.nuxeo.ecm.platform.site.SiteRequest;
import org.nuxeo.ecm.platform.site.SiteRoot;
import org.nuxeo.ecm.platform.site.servlet.SiteServlet;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.scripting.ScriptingService;
import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyTuple;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class Scripting {

    RenderingEngine renderingEngine;
    ScriptingService  scriptService;

    private static final String CHAR_FILE_EXT = "html htm xml css txt java c cpp h";
    private static final String BINARY_FILE_EXT = "gif jpg jpeg png pdf doc xsl";

    public Scripting(RenderingEngine engine) {
        renderingEngine = engine;
        scriptService = Framework.getLocalService(ScriptingService.class);
        if (scriptService == null) {
            throw new RuntimeException("Scripting is not enabled: Put nuxeo-runtime-scripting in the classpath");
        }
    }

    public RenderingEngine getRenderingEngine() {
        return renderingEngine;
    }


    public void exec(SiteRequest req, ScriptFile script) throws Exception {
        String ext = script.getExtension();
        if ("ftl".equals(ext)) {
            req.render(script.getPath());
        } else {
            runScript(req, script);
        }
    }

    public void exec(SiteRequest req) throws Exception {
        ScriptFile script = req.getTargetScript();
        if (script.getFile().isFile()) {
            exec(req, script);
        } else {
            SiteRoot root = req.getRoot();
            script = root.getScript(root.getDefaultPage(), null);
            exec(req, script);
        }
    }

    protected void runScript(SiteRequest req, ScriptFile script) throws Exception {
        String ext = script.getExtension();
        ScriptEngine engine = scriptService.getScriptEngineManager().getEngineByExtension(ext);
        if (engine != null) {
            try {
                Reader reader = new FileReader(script.getFile());
                try {
                    ScriptContext ctx = new SimpleScriptContext();
                    ctx.setAttribute("req", req, ScriptContext.ENGINE_SCOPE);
                    ctx.setAttribute("scripting", this, ScriptContext.ENGINE_SCOPE);
                    ctx.setAttribute("out", req.getResponse().getWriter(), ScriptContext.ENGINE_SCOPE);
                    engine.eval(reader, ctx);
                } finally {
                    reader.close();
                }
            } catch (IOException e) {
                throw new ScriptException(e);
            }
        } else {
            if (CHAR_FILE_EXT.contains(ext)) { //TODO use char writer instead of stream
                FileInputStream in = new FileInputStream(script.getFile());
                try {
                    FileUtils.copy(in, req.getResponse().getOutputStream());
                } finally {
                    if (in != null) in.close();
                }
            } else if (BINARY_FILE_EXT.contains(ext)) {
                FileInputStream in = new FileInputStream(script.getFile());
                try {
                    FileUtils.copy(in, req.getResponse().getOutputStream());
                } finally {
                    if (in != null) in.close();
                }
            } else {
                throw new ScriptException(
                        "No script engine was found for the file: " + script.getPath());
            }
        }
    }


    @SuppressWarnings("unchecked")
    public static Map convertPythonMap(PyDictionary dict) {
        PyList list = dict.items();
        Map table = new HashMap();
        for(int i = list.__len__(); i-- >  0; ) {
            PyTuple tup = (PyTuple)list.__getitem__(i);
            String key = tup.__getitem__(0).toString();
            table.put(key, tup.__getitem__(1));
        }
        return table;
    }

}