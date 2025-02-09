package com.dimensiondelvers.dimensiondelvers.riftmap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Quaternionf;

public class Camera {
    private Vector3f position;
    private Quaternionf rotation;
    private Matrix4f projectionMatrix;

    public Camera(float fov, float aspectRatio, float near, float far) {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Quaternionf();
        this.projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, near, far);
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation.set(rotation);
    }

    public void setRotationFromYawPitch(float yaw, float pitch) {
        this.rotation.identity().rotateY((float) Math.toRadians(-yaw)).rotateX((float) Math.toRadians(-pitch));
    }

    public void move(float dx, float dy, float dz) {
        Vector3f direction = new Vector3f(dx, dy, dz).rotate(rotation);
        this.position.add(direction);
    }

    public Matrix4f getViewMatrix() {
        Matrix4f viewMatrix = new Matrix4f().identity();
        viewMatrix.rotate(rotation.conjugate(new Quaternionf()));
        viewMatrix.translate(-position.x, -position.y, -position.z);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}