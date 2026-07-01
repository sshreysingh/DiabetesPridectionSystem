package com.diabetes.prediction.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PredictionDatabase_Impl extends PredictionDatabase {
  private volatile PredictionDao _predictionDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `predictions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pregnancies` REAL NOT NULL, `glucose` REAL NOT NULL, `bloodPressure` REAL NOT NULL, `skinThickness` REAL NOT NULL, `insulin` REAL NOT NULL, `bmi` REAL NOT NULL, `diabetesPedigreeFunction` REAL NOT NULL, `age` REAL NOT NULL, `prediction` INTEGER NOT NULL, `probability` REAL NOT NULL, `message` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ac1a8ada751de1d721ef430dca5e268e')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `predictions`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPredictions = new HashMap<String, TableInfo.Column>(13);
        _columnsPredictions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("pregnancies", new TableInfo.Column("pregnancies", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("glucose", new TableInfo.Column("glucose", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("bloodPressure", new TableInfo.Column("bloodPressure", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("skinThickness", new TableInfo.Column("skinThickness", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("insulin", new TableInfo.Column("insulin", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("bmi", new TableInfo.Column("bmi", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("diabetesPedigreeFunction", new TableInfo.Column("diabetesPedigreeFunction", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("age", new TableInfo.Column("age", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("prediction", new TableInfo.Column("prediction", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("probability", new TableInfo.Column("probability", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPredictions.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPredictions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPredictions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPredictions = new TableInfo("predictions", _columnsPredictions, _foreignKeysPredictions, _indicesPredictions);
        final TableInfo _existingPredictions = TableInfo.read(db, "predictions");
        if (!_infoPredictions.equals(_existingPredictions)) {
          return new RoomOpenHelper.ValidationResult(false, "predictions(com.diabetes.prediction.data.db.PredictionEntity).\n"
                  + " Expected:\n" + _infoPredictions + "\n"
                  + " Found:\n" + _existingPredictions);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ac1a8ada751de1d721ef430dca5e268e", "f75c703bcc63bbd09bbabe8a65b23dd6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "predictions");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `predictions`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PredictionDao.class, PredictionDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PredictionDao predictionDao() {
    if (_predictionDao != null) {
      return _predictionDao;
    } else {
      synchronized(this) {
        if(_predictionDao == null) {
          _predictionDao = new PredictionDao_Impl(this);
        }
        return _predictionDao;
      }
    }
  }
}
