// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Hello.proto

package lam.serialization.protobuf;

public final class HelloProto {
  private HelloProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface HelloOrBuilder extends
      // @@protoc_insertion_point(interface_extends:lam.serialization.protobuf.Hello)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required sint32 kk = 1;</code>
     */
    boolean hasKk();
    /**
     * <code>required sint32 kk = 1;</code>
     */
    int getKk();

    /**
     * <code>required sint64 bb = 2;</code>
     */
    boolean hasBb();
    /**
     * <code>required sint64 bb = 2;</code>
     */
    long getBb();

    /**
     * <code>optional int32 gender = 3;</code>
     */
    boolean hasGender();
    /**
     * <code>optional int32 gender = 3;</code>
     */
    int getGender();
  }
  /**
   * Protobuf type {@code lam.serialization.protobuf.Hello}
   */
  public  static final class Hello extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:lam.serialization.protobuf.Hello)
      HelloOrBuilder {
    // Use Hello.newBuilder() to construct.
    private Hello(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Hello() {
      kk_ = 0;
      bb_ = 0L;
      gender_ = 0;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Hello(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              kk_ = input.readSInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              bb_ = input.readSInt64();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              gender_ = input.readInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return lam.serialization.protobuf.HelloProto.internal_static_lam_serialization_protobuf_Hello_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return lam.serialization.protobuf.HelloProto.internal_static_lam_serialization_protobuf_Hello_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              lam.serialization.protobuf.HelloProto.Hello.class, lam.serialization.protobuf.HelloProto.Hello.Builder.class);
    }

    private int bitField0_;
    public static final int KK_FIELD_NUMBER = 1;
    private int kk_;
    /**
     * <code>required sint32 kk = 1;</code>
     */
    public boolean hasKk() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required sint32 kk = 1;</code>
     */
    public int getKk() {
      return kk_;
    }

    public static final int BB_FIELD_NUMBER = 2;
    private long bb_;
    /**
     * <code>required sint64 bb = 2;</code>
     */
    public boolean hasBb() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required sint64 bb = 2;</code>
     */
    public long getBb() {
      return bb_;
    }

    public static final int GENDER_FIELD_NUMBER = 3;
    private int gender_;
    /**
     * <code>optional int32 gender = 3;</code>
     */
    public boolean hasGender() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional int32 gender = 3;</code>
     */
    public int getGender() {
      return gender_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasKk()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasBb()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeSInt32(1, kk_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeSInt64(2, bb_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, gender_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeSInt32Size(1, kk_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeSInt64Size(2, bb_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, gender_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof lam.serialization.protobuf.HelloProto.Hello)) {
        return super.equals(obj);
      }
      lam.serialization.protobuf.HelloProto.Hello other = (lam.serialization.protobuf.HelloProto.Hello) obj;

      boolean result = true;
      result = result && (hasKk() == other.hasKk());
      if (hasKk()) {
        result = result && (getKk()
            == other.getKk());
      }
      result = result && (hasBb() == other.hasBb());
      if (hasBb()) {
        result = result && (getBb()
            == other.getBb());
      }
      result = result && (hasGender() == other.hasGender());
      if (hasGender()) {
        result = result && (getGender()
            == other.getGender());
      }
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      if (hasKk()) {
        hash = (37 * hash) + KK_FIELD_NUMBER;
        hash = (53 * hash) + getKk();
      }
      if (hasBb()) {
        hash = (37 * hash) + BB_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
            getBb());
      }
      if (hasGender()) {
        hash = (37 * hash) + GENDER_FIELD_NUMBER;
        hash = (53 * hash) + getGender();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static lam.serialization.protobuf.HelloProto.Hello parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static lam.serialization.protobuf.HelloProto.Hello parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static lam.serialization.protobuf.HelloProto.Hello parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static lam.serialization.protobuf.HelloProto.Hello parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static lam.serialization.protobuf.HelloProto.Hello parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static lam.serialization.protobuf.HelloProto.Hello parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static lam.serialization.protobuf.HelloProto.Hello parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static lam.serialization.protobuf.HelloProto.Hello parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static lam.serialization.protobuf.HelloProto.Hello parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static lam.serialization.protobuf.HelloProto.Hello parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(lam.serialization.protobuf.HelloProto.Hello prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code lam.serialization.protobuf.Hello}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:lam.serialization.protobuf.Hello)
        lam.serialization.protobuf.HelloProto.HelloOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return lam.serialization.protobuf.HelloProto.internal_static_lam_serialization_protobuf_Hello_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return lam.serialization.protobuf.HelloProto.internal_static_lam_serialization_protobuf_Hello_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                lam.serialization.protobuf.HelloProto.Hello.class, lam.serialization.protobuf.HelloProto.Hello.Builder.class);
      }

      // Construct using lam.serialization.protobuf.HelloProto.Hello.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        kk_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        bb_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        gender_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return lam.serialization.protobuf.HelloProto.internal_static_lam_serialization_protobuf_Hello_descriptor;
      }

      public lam.serialization.protobuf.HelloProto.Hello getDefaultInstanceForType() {
        return lam.serialization.protobuf.HelloProto.Hello.getDefaultInstance();
      }

      public lam.serialization.protobuf.HelloProto.Hello build() {
        lam.serialization.protobuf.HelloProto.Hello result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public lam.serialization.protobuf.HelloProto.Hello buildPartial() {
        lam.serialization.protobuf.HelloProto.Hello result = new lam.serialization.protobuf.HelloProto.Hello(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.kk_ = kk_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.bb_ = bb_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.gender_ = gender_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof lam.serialization.protobuf.HelloProto.Hello) {
          return mergeFrom((lam.serialization.protobuf.HelloProto.Hello)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(lam.serialization.protobuf.HelloProto.Hello other) {
        if (other == lam.serialization.protobuf.HelloProto.Hello.getDefaultInstance()) return this;
        if (other.hasKk()) {
          setKk(other.getKk());
        }
        if (other.hasBb()) {
          setBb(other.getBb());
        }
        if (other.hasGender()) {
          setGender(other.getGender());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        if (!hasKk()) {
          return false;
        }
        if (!hasBb()) {
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        lam.serialization.protobuf.HelloProto.Hello parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (lam.serialization.protobuf.HelloProto.Hello) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int kk_ ;
      /**
       * <code>required sint32 kk = 1;</code>
       */
      public boolean hasKk() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required sint32 kk = 1;</code>
       */
      public int getKk() {
        return kk_;
      }
      /**
       * <code>required sint32 kk = 1;</code>
       */
      public Builder setKk(int value) {
        bitField0_ |= 0x00000001;
        kk_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required sint32 kk = 1;</code>
       */
      public Builder clearKk() {
        bitField0_ = (bitField0_ & ~0x00000001);
        kk_ = 0;
        onChanged();
        return this;
      }

      private long bb_ ;
      /**
       * <code>required sint64 bb = 2;</code>
       */
      public boolean hasBb() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required sint64 bb = 2;</code>
       */
      public long getBb() {
        return bb_;
      }
      /**
       * <code>required sint64 bb = 2;</code>
       */
      public Builder setBb(long value) {
        bitField0_ |= 0x00000002;
        bb_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required sint64 bb = 2;</code>
       */
      public Builder clearBb() {
        bitField0_ = (bitField0_ & ~0x00000002);
        bb_ = 0L;
        onChanged();
        return this;
      }

      private int gender_ ;
      /**
       * <code>optional int32 gender = 3;</code>
       */
      public boolean hasGender() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional int32 gender = 3;</code>
       */
      public int getGender() {
        return gender_;
      }
      /**
       * <code>optional int32 gender = 3;</code>
       */
      public Builder setGender(int value) {
        bitField0_ |= 0x00000004;
        gender_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 gender = 3;</code>
       */
      public Builder clearGender() {
        bitField0_ = (bitField0_ & ~0x00000004);
        gender_ = 0;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:lam.serialization.protobuf.Hello)
    }

    // @@protoc_insertion_point(class_scope:lam.serialization.protobuf.Hello)
    private static final lam.serialization.protobuf.HelloProto.Hello DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new lam.serialization.protobuf.HelloProto.Hello();
    }

    public static lam.serialization.protobuf.HelloProto.Hello getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<Hello>
        PARSER = new com.google.protobuf.AbstractParser<Hello>() {
      public Hello parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new Hello(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Hello> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Hello> getParserForType() {
      return PARSER;
    }

    public lam.serialization.protobuf.HelloProto.Hello getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_lam_serialization_protobuf_Hello_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_lam_serialization_protobuf_Hello_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\013Hello.proto\022\032lam.serialization.protobu" +
      "f\"/\n\005Hello\022\n\n\002kk\030\001 \002(\021\022\n\n\002bb\030\002 \002(\022\022\016\n\006ge" +
      "nder\030\003 \001(\005B(\n\032lam.serialization.protobuf" +
      "B\nHelloProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_lam_serialization_protobuf_Hello_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_lam_serialization_protobuf_Hello_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_lam_serialization_protobuf_Hello_descriptor,
        new java.lang.String[] { "Kk", "Bb", "Gender", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
