package io.github.lvrodrigues.noblood;

import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import java.util.concurrent.Executors;

public class NoBloodWorker extends ListenableWorker {

    private final ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());

    private static final int NOTIFICATION_ID = 101;

    private static final String LOGTAG = "NoBlookWorker";

    private SettableFuture<Result> resultFuture;

    /**
     * @param context   The application {@link Context}
     * @param params    Parameters to setup the internal state of this worker
     */
    public NoBloodWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        Log.i(LOGTAG, "Criando processo em segundo plano.");
        resultFuture = SettableFuture.create();
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        Log.d(LOGTAG, "Sinalizando como tarefa de longa duração.");
        setForegroundAsync(createForegroundInfo());

        Log.d(LOGTAG, "Inicializando o serviço em segundo plano.");
        ListenableFuture<Result> future = executor.submit(() -> {
            int value = 0;
            while (!isStopped()) {
                value += 1;
                value = (value % 100);
                Data data = new Data.Builder().putInt("progress", value).build();
                setProgressAsync(data);
                Log.v(LOGTAG, "Processando... (" + value + ")");
                Thread.sleep(1 * 1000 * 60);
            }
            return Result.success();
        });

        Log.d(LOGTAG, "Preparando callbacks...");
        Futures.addCallback(future, new FutureCallback<Result>() {
            @Override
            public void onSuccess(Result result) {
                Log.d(LOGTAG, "Worker concluiu com sucesso.");
                resultFuture.set(result);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOGTAG, "Falha no processamento do Worker.");
                resultFuture.setException(t);
            }
        }, executor);

        Log.d(LOGTAG, "Retornando objeto futuro...");
        return resultFuture;
    }

    private ForegroundInfo createForegroundInfo() {
        Log.d(LOGTAG, "Apresentando a notificação.");
        Context context = getApplicationContext();
        Notification notification = new NotificationCompat.Builder(context, NoBloodApplication.CHANNEL_ID)
                .setContentTitle(context.getText(R.string.app_name))
                .setContentText(context.getText(R.string.notification_message))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_foreground))
                .setTicker(context.getText(R.string.app_name))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setShowWhen(false)
                .setOngoing(true)
                .setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
                .build();
        return new ForegroundInfo(NOTIFICATION_ID, notification);
    }
}
