package estthgapp.com.fixlib;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixDexUtils {

    // classes2.dex classes3.dex
    private static HashSet<File> LoadedDex = new HashSet<>();

    static {
        // 修复代码之前，先清理集合
        LoadedDex.clear();
    }

    public static void loadFixedDex(Context context) {
        File fileDir = context.getDir(Constants.DEX_DIR, Context.MODE_PRIVATE);
        File[] listFile = fileDir.listFiles();
        // 循环私有目录下的所有文件
        for (File file : listFile) {
            LoadedDex.add(file);
        }
        // 创建类加载器
        createDexClassLoader(context, fileDir);

    }

    private static void createDexClassLoader(Context context, File fileDir) {
        // 创建解压目录
        String optDir = fileDir.getAbsolutePath() + File.separator + "opt_dex";
        // 创建目录
        File fopt = new File(optDir);
        if (!fopt.exists()) {
            fopt.mkdirs();
        }
        for (File dex : LoadedDex) {
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(), optDir,
                    null, context.getClassLoader());
            // 没循环一次 修复一次
            fix(classLoader, context);

        }

    }

    private static void fix(DexClassLoader classLoader, Context context) {
        // 获取系统的PathClassLoader
        PathClassLoader pathLoader = (PathClassLoader) context.getClassLoader();

        try {
            // 获取自有的dexElements数组
            Object myElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(classLoader));
            // 获取系统的dexElements数组
            Object systemElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(pathLoader));
            // 合并并生成新的dexElements数组
            Object dexElements = ArrayUtils.combineArray(myElements, systemElements);
            // 获取系统的pathList
            Object systemPathList = ReflectUtils.getPathList(pathLoader);
            // 通过反射技术，将新的dexElements数组赋值给系统的pathList
            ReflectUtils.setField(systemElements, systemPathList.getClass(), dexElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
