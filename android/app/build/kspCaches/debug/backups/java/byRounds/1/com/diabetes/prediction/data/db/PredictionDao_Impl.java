package com.diabetes.prediction.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PredictionDao_Impl implements PredictionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PredictionEntity> __insertionAdapterOfPredictionEntity;

  private final EntityDeletionOrUpdateAdapter<PredictionEntity> __deletionAdapterOfPredictionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public PredictionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPredictionEntity = new EntityInsertionAdapter<PredictionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `predictions` (`id`,`pregnancies`,`glucose`,`bloodPressure`,`skinThickness`,`insulin`,`bmi`,`diabetesPedigreeFunction`,`age`,`prediction`,`probability`,`message`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PredictionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getPregnancies());
        statement.bindDouble(3, entity.getGlucose());
        statement.bindDouble(4, entity.getBloodPressure());
        statement.bindDouble(5, entity.getSkinThickness());
        statement.bindDouble(6, entity.getInsulin());
        statement.bindDouble(7, entity.getBmi());
        statement.bindDouble(8, entity.getDiabetesPedigreeFunction());
        statement.bindDouble(9, entity.getAge());
        statement.bindLong(10, entity.getPrediction());
        statement.bindDouble(11, entity.getProbability());
        statement.bindString(12, entity.getMessage());
        statement.bindLong(13, entity.getTimestamp());
      }
    };
    this.__deletionAdapterOfPredictionEntity = new EntityDeletionOrUpdateAdapter<PredictionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `predictions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PredictionEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM predictions";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final PredictionEntity prediction,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPredictionEntity.insertAndReturnId(prediction);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final PredictionEntity prediction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPredictionEntity.handle(prediction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PredictionEntity>> getAllPredictions() {
    final String _sql = "SELECT * FROM predictions ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"predictions"}, new Callable<List<PredictionEntity>>() {
      @Override
      @NonNull
      public List<PredictionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPregnancies = CursorUtil.getColumnIndexOrThrow(_cursor, "pregnancies");
          final int _cursorIndexOfGlucose = CursorUtil.getColumnIndexOrThrow(_cursor, "glucose");
          final int _cursorIndexOfBloodPressure = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressure");
          final int _cursorIndexOfSkinThickness = CursorUtil.getColumnIndexOrThrow(_cursor, "skinThickness");
          final int _cursorIndexOfInsulin = CursorUtil.getColumnIndexOrThrow(_cursor, "insulin");
          final int _cursorIndexOfBmi = CursorUtil.getColumnIndexOrThrow(_cursor, "bmi");
          final int _cursorIndexOfDiabetesPedigreeFunction = CursorUtil.getColumnIndexOrThrow(_cursor, "diabetesPedigreeFunction");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfPrediction = CursorUtil.getColumnIndexOrThrow(_cursor, "prediction");
          final int _cursorIndexOfProbability = CursorUtil.getColumnIndexOrThrow(_cursor, "probability");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<PredictionEntity> _result = new ArrayList<PredictionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PredictionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final float _tmpPregnancies;
            _tmpPregnancies = _cursor.getFloat(_cursorIndexOfPregnancies);
            final float _tmpGlucose;
            _tmpGlucose = _cursor.getFloat(_cursorIndexOfGlucose);
            final float _tmpBloodPressure;
            _tmpBloodPressure = _cursor.getFloat(_cursorIndexOfBloodPressure);
            final float _tmpSkinThickness;
            _tmpSkinThickness = _cursor.getFloat(_cursorIndexOfSkinThickness);
            final float _tmpInsulin;
            _tmpInsulin = _cursor.getFloat(_cursorIndexOfInsulin);
            final float _tmpBmi;
            _tmpBmi = _cursor.getFloat(_cursorIndexOfBmi);
            final float _tmpDiabetesPedigreeFunction;
            _tmpDiabetesPedigreeFunction = _cursor.getFloat(_cursorIndexOfDiabetesPedigreeFunction);
            final float _tmpAge;
            _tmpAge = _cursor.getFloat(_cursorIndexOfAge);
            final int _tmpPrediction;
            _tmpPrediction = _cursor.getInt(_cursorIndexOfPrediction);
            final float _tmpProbability;
            _tmpProbability = _cursor.getFloat(_cursorIndexOfProbability);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new PredictionEntity(_tmpId,_tmpPregnancies,_tmpGlucose,_tmpBloodPressure,_tmpSkinThickness,_tmpInsulin,_tmpBmi,_tmpDiabetesPedigreeFunction,_tmpAge,_tmpPrediction,_tmpProbability,_tmpMessage,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM predictions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
