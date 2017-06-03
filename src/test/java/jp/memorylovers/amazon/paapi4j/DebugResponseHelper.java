package jp.memorylovers.amazon.paapi4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.simpleframework.xml.core.Persister;

import jp.memorylovers.amazon.paapi4j.request.RequestItemLookup;
import jp.memorylovers.amazon.paapi4j.request.RequestItemSearch;
import jp.memorylovers.amazon.paapi4j.response.Response;
import jp.memorylovers.amazon.paapi4j.response.ResponseHelper;
import jp.memorylovers.amazon.paapi4j.utils.Utils;

public class DebugResponseHelper extends ResponseHelper {
    private static final String OUTPUT_DIR = "out";

    @Override
    protected Response deserialize(String responseBodsy) throws Exception {
        dumpFile(responseBodsy);
        return new Persister().read(Response.class, responseBodsy, false);
    }

    private File dumpFile(String contents) throws IOException {
        File outDir = new File(OUTPUT_DIR);

        if (!outDir.exists()) {
            outDir.mkdir();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(request.operation());
        sb.append("_");

        if (request instanceof RequestItemLookup) {
            sb.append(((RequestItemLookup) request).getResponseGroup());
            sb.append("_");
        } else if (request instanceof RequestItemSearch) {
            sb.append(((RequestItemSearch) request).getResponseGroup());
            sb.append("_");
        }

        sb.append(Utils.getCurDateTIme());
        sb.append(".xml");

        File outFile = new File(outDir, sb.toString());

        System.out.println("output: " + outFile.getAbsolutePath());
        Files.write(outFile.toPath(), contents.getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        return outFile;
    }
}
