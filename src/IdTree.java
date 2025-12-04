public class IdTree {
    private class TreeNode {
        String id;
        Student data;
        TreeNode left, right;
        TreeNode(Student s) {
            this.id = s.getId();
            this.data = s;
            this.left = this.right = null;
        }
    }

    private TreeNode root;
    public void insert(Student s) {
        root = insertRec(root, s);
    }

    private TreeNode insertRec(TreeNode root, Student s) {
        if (root == null) return new TreeNode(s);
        if (s.getId().compareTo(root.id) < 0) root.left = insertRec(root.left, s);
        else if (s.getId().compareTo(root.id) > 0) root.right = insertRec(root.right, s);
        return root;
    }
    public Student search(String id) {
        return searchRec(root, id);
    }

    private Student searchRec(TreeNode root, String id) {
        if (root == null) return null;
        if (id.equals(root.id)) return root.data;
        return (id.compareTo(root.id) < 0) ? searchRec(root.left, id) : searchRec(root.right, id);
    }

    public boolean contains(String id) {
        return search(id) != null;
    }

    public void delete(String id) {
        root = deleteRec(root, id);
    }

    private TreeNode deleteRec(TreeNode root, String id) {
        if (root == null) return root;

        if (id.compareTo(root.id) < 0) root.left = deleteRec(root.left, id);
        else if (id.compareTo(root.id) > 0) root.right = deleteRec(root.right, id);
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;
            root.id = minValue(root.right);
            root.right = deleteRec(root.right, root.id);
        }
        return root;
    }

    private String minValue(TreeNode root) {
        String minv = root.id;
        while (root.left != null) {
            minv = root.left.id;
            root = root.left;
        }
        return minv;
    }
}